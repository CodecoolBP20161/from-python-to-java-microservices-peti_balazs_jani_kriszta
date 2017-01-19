package com.codecool.reviewservice.dao.implementation;

import com.codecool.reviewservice.dao.connection.DBConnection;
import com.codecool.reviewservice.dao.ReviewDao;
import com.codecool.reviewservice.model.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ReviewDaoJdbc implements ReviewDao {
    private static final Logger logger = LoggerFactory.getLogger(ReviewDaoJdbc.class);
    private DBConnection connection = new DBConnection();
    private static ReviewDaoJdbc instance = null;
//    public ArrayList<Review> reviews = new ArrayList<>();
    private String sql;

    public static ReviewDaoJdbc getInstance(){
        if (instance == null){
            instance = new ReviewDaoJdbc();
            logger.info("Create a new ReviewDaoJdbc Singleton "+instance);
        }
        return instance;
    }

    public void add(Review reviewModel) {
        int clientId = getClientId(reviewModel.getClientID());
        String productName = reviewModel.getProductName();
        String comment = reviewModel.getComment();
        int ratings = reviewModel.getRating();
        String reviewKey = reviewModel.getReviewKey();
        String status = reviewModel.getStatus();

        sql = "INSERT INTO review (client_id, product_name, comment, ratings, review_key, status) " +
                "VALUES("+clientId+",'"+productName+"','"+comment+"',"+ratings+",'"+reviewKey+"', '"+status+"');";
        connection.executeQuery(sql);
        logger.debug("Save to database | Review model: "+reviewModel);
    }

    public void remove(int reviewKey) {
        sql = "DELETE FROM review WHERE review_key='"+reviewKey+"';";
        executeQuery(sql);
        logger.debug("Delete from database | ReviewKey: "+reviewKey);
    }

    public ArrayList<Review> getByClientID(int clientID) {
        sql = "SELECT * FROM review WHERE id="+clientID+";";
        logger.debug("Get a review by client_id("+clientID+") | Review model: "+createReviewModel(sql));
        return createReviewModel(sql);
    }

    public ArrayList<Review> getByProductName(String productName) {
        sql = "SELECT * FROM review WHERE product_name='"+productName+"';";
        logger.debug("Get a review by product_name("+productName+") | Review model: "+createReviewModel(sql));
        return createReviewModel(sql);
    }

    public ArrayList<Review> getApprovedByProductName(String productName) {
        sql = "SELECT * FROM review WHERE product_name='"+productName+"' and status='APPROVED';";
        logger.debug("Get a review by product_name "+productName+" if status is APPROVED | Review model: "+createReviewModel(sql));
        return createReviewModel(sql);
    }
    public ArrayList<Review> getApprovedByClientId(int clientID) {
        sql = "SELECT * FROM review WHERE client_id="+clientID+" and status='APPROVED';";
        logger.debug("Get a review by client_id "+clientID+" if status is APPROVED | Review model: "+createReviewModel(sql));
        return createReviewModel(sql);
    }
    public void updateStatus(String review_key, String newStatus){
        sql = "UPDATE review SET status='"+newStatus+"' WHERE review_key='"+review_key+"';";
        executeQuery(sql);
        logger.info("Update review status where review_key: "+review_key+" | new status: "+newStatus);
    }
    private int getClientId(int clientId){
        sql = "SELECT id FROM client WHERE id="+clientId+";";
        try (Connection conn = connection.connect();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(sql)){
            if (rs.next()){
                return rs.getInt("id");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    private ArrayList<Review> createReviewModel(String sql){
        ArrayList<Review> reviews = new ArrayList<>();
        try (Connection conn = connection.connect();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(sql)){

            while (rs.next()) {
                Review review = new Review(rs.getInt("client_id"),
                        rs.getString("product_name"),
                        rs.getString("comment"),
                        rs.getInt("ratings"),
                        rs.getString("review_key"),
                        rs.getString("status"));
                review.setId(rs.getString("id"));
                reviews.add(review);
                logger.info("review object: "+review.getComment());
            }
            logger.info("review list: "+reviews);
                return reviews;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void executeQuery(String sql){
        connection.executeQuery(sql);
    }
}
