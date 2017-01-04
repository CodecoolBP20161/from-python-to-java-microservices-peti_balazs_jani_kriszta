package com.codecool.reviewservice.dao;

import com.codecool.reviewservice.model.Review;

import java.util.ArrayList;

public interface ReviewDao {
    void add(Review reviewModel);
    void remove(int id);
    ArrayList<Review> getByClientID(int clientID);
    ArrayList<Review> getByProductName(String productName);
    ArrayList<Review> getApprovedByClientId(int clientID);
    ArrayList<Review> getApprovedByProductName(String productName);
}
