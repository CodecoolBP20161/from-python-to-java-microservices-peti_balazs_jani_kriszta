package com.codecool.reviewservice.controller;

import com.codecool.reviewservice.dao.ClientDao;
import com.codecool.reviewservice.dao.ReviewDao;
import com.codecool.reviewservice.dao.implementation.ClientDaoJdbc;
import com.codecool.reviewservice.dao.implementation.ReviewDaoJdbc;
import com.codecool.reviewservice.email.Email;
import com.codecool.reviewservice.errorHandling.InvalidClient;
import com.codecool.reviewservice.model.Client;
import com.codecool.reviewservice.model.Review;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;


public class ReviewController {
    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    private static ReviewDao reviews = ReviewDaoJdbc.getInstance();
    private static ClientDao clients = ClientDaoJdbc.getInstance();

    public static final String API_KEY_PARAM = "APIKey";
    public static final String PRODUCT_NAME_PARAM = "productName";


    public static String newReview(Request request, Response response) throws IOException, URISyntaxException, InvalidClient {
        String APIKey = request.params("APIKey");
        logger.info("New review from client with APIKey: " + APIKey + ", and Product: " + request.params("productName"));

        if (!validateClient(APIKey)) {
            response.status(404);
            throw new InvalidClient("Client is not found in database.");
        } else {
            Review newReview = new Review(getClientID(APIKey),
                    request.params("productName"),
                    request.body(),
                    Integer.parseInt(request.params("ratings")));
            reviews.add(newReview);
            Email.ReviewForModerationEmail(newReview);
            response.status(200);
            return null;
        }
    }

    public static String changeStatus(Request request, Response response) throws IOException, URISyntaxException, InvalidClient {
        logger.info("Changing status of review...");
        String APIKey = request.params("APIKey");

        if (!validateClient(APIKey)) {
            throw new InvalidClient("Client is not found in database.");
        } else {
            String reviewKey = request.params("reviewKey");
            String status = request.params("status").toUpperCase();
            reviews.updateStatus(reviewKey, status);
            response.redirect("/newStatus");
            return null;
        }
    }

    public static String getAllReviewFromClient(Request request, Response response) throws IOException, URISyntaxException, InvalidClient {
        ArrayList<String> reviewsOfClient = new ArrayList<>();

        String APIKey = request.params("APIKey");

        if (!validateClient(APIKey)) {
            throw new InvalidClient("Client is not found in database.");
        } else {
            ArrayList<Review> returnReviews = reviews.getApprovedByClientId(getClientID(APIKey));
            for (Review review : returnReviews) {
                reviewsOfClient.add(review.toString());
            }

            return jsonify(reviewsOfClient);
        }
    }

    public static String getAllReviewOfProduct(Request request, Response response) throws IOException, URISyntaxException, InvalidClient {
        String APIKey = request.params(API_KEY_PARAM);
        String productName = request.params(PRODUCT_NAME_PARAM);

        logger.info("Request from client with APIKey: " + APIKey);
        logger.info("Request for all approved reviews of: " + productName);

        ArrayList<String> approvedReviews = new ArrayList<>();

        if (!validateClient(APIKey)) {
            throw new InvalidClient("Client is not found in database.");
        } else {
            ArrayList<Review> returnReviews = reviews.getApprovedByProductName(productName.replace(" ", "").toUpperCase());
            logger.info("Converting review objects to string: " + returnReviews);
            for (Review review : returnReviews) {
                approvedReviews.add(review.toString());
            }
            return jsonify(approvedReviews);
        }
    }

    private static boolean validateClient(String APIKey) {
        Client client = clients.getByAPIKey(APIKey);
        if (client == null) {
            return false;
        }
        return true;
    }

    private static int getClientID(String APIKey){
        return clients.getByAPIKey(APIKey).getId();
    }

    private static String jsonify(ArrayList<String> list) {
        String result = new Gson().toJson(list);
        logger.info("Reviews jasonified: " + result);
        return new Gson().toJson(list);
    }

}