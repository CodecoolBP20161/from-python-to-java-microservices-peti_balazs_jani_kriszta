package com.codecool.reviewservice.controller;

import com.codecool.reviewservice.dao.ClientDao;
import com.codecool.reviewservice.dao.ReviewDao;
import com.codecool.reviewservice.dao.implementation.ClientDaoJdbc;
import com.codecool.reviewservice.dao.implementation.ReviewDaoJdbc;
import com.codecool.reviewservice.email.Email;
import com.codecool.reviewservice.errorHandling.InvalidClient;
import com.codecool.reviewservice.model.Client;
import com.codecool.reviewservice.model.Review;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;


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

    public static JSONObject getAllReviewFromClient(Request request, Response response) throws IOException, URISyntaxException, InvalidClient {
        String APIKey = request.params(API_KEY_PARAM);
        int id = getClientID(APIKey);

        logger.info("Getting all approved reviews from client with APIKey: " + APIKey);

        if (!validateClient(APIKey)) {
            throw new InvalidClient("Client is not found in database.");
        }  else {
            ArrayList<Review> returnReviews = reviews.getApprovedByClientId(id);
            return createJSONObject(returnReviews);
        }
    }

    public static JSONObject getAllReviewOfProduct(Request request, Response response) throws IOException, URISyntaxException, InvalidClient {
        String APIKey = request.params(API_KEY_PARAM);
        String productName = request.params(PRODUCT_NAME_PARAM);
        logger.info("Request from client with APIKey: " + APIKey+ " for all approved reviews of: " + productName);

        if (!validateClient(APIKey)) {
            throw new InvalidClient("Client is not found in database.");
        } else {
            ArrayList<Review> returnReviews = reviews.getApprovedByProductName(productName.replace(" ", "").toUpperCase());
            return createJSONObject(returnReviews);
        }
    }

    private static JSONObject createJSONObject(ArrayList<Review> reviews){
        JSONObject json = new JSONObject();
        Integer idx = 1;
        for (Review review : reviews) {
            json.put(Integer.toString(idx), buildReviewMap(review));
            idx++;
        }
        return json;
    }

    private static HashMap<String, String> buildReviewMap(Review review){
        HashMap<String, String> mapReview = new HashMap<>();
        mapReview.put("productName", review.getProductName());
        mapReview.put("comment", review.getComment());
        mapReview.put("ratings", String.valueOf(review.getRating()));
        mapReview.put("reviewKey", review.getReviewKey());
        return mapReview;
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
}