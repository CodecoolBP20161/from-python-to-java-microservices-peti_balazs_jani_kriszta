package com.codecool.reviewservice.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;


public class Review {
    private static final Logger logger = LoggerFactory.getLogger(Review.class);

    private String id;
    private int clientID;
    private String productName;
    private String comment;
    private int rating;
    private String status;
    private String reviewKey;

//    Constructor without reviewKey, it generates new
    public Review(int clientID, String productName, String comment, int rating) {
        this.clientID = clientID;
        this.productName = handleProductName(productName);
        this.comment = comment;
        this.rating = rating;
        this.status = Status.PENDING.toString();
        this.reviewKey = UUID.randomUUID().toString();

        logger.info("Create new Review model with new reviewKey: " + this.reviewKey + " Status: " + this.status);
    }

//    Constructor with reviewKey
    public Review(int clientID, String productName, String comment, int rating, String reviewKey, String status) {
        this.clientID = clientID;
        this.productName = productName;
        this.comment = comment;
        this.rating = rating;
        this.reviewKey = reviewKey;
        this.status = status;

        logger.info("Create Review model with the reviewKey: " + this.reviewKey + " Status: " + this.status);

    }

    public String getStatus() {
        return status;
    }

    public void setStatusApproved() {
        logger.info("Set status to: " + Status.APPROVED);
        this.status = Status.APPROVED.toString();
    }

    public void setStatusDenied() {
        logger.info("Set status to: " + Status.DENIED);
        this.status = Status.DENIED.toString();
    }

    private String handleProductName(String prodName){
        return prodName.replace(" ", "").toUpperCase();
    }

    public String getProductName() {
        return productName;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getClientID() {
        return clientID;
    }

    public String getComment() {
        return comment;
    }

    public int getRating() {
        return rating;
    }

    public String getReviewKey() {
        return reviewKey;
    }

    @Override
    public String toString(){
        return String.format(
                        "Client_id: %s \n" +
                        "Product name: %s \n" +
                        "Review: %s \n" +
                        "Ratings: %d \n" +
                        "Review Key: %s \n" +
                        "Status: %s", getClientID(),getProductName(),getComment(),getRating(),getReviewKey(),getStatus());
    }
}
