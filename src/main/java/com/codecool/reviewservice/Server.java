package com.codecool.reviewservice;

import com.codecool.reviewservice.controller.ClientController;
import com.codecool.reviewservice.controller.RegistrationPageController;
import com.codecool.reviewservice.controller.ReviewController;
import com.codecool.reviewservice.dao.connection.DBConnection;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.sql.SQLException;

import static spark.Spark.*;

public class Server {


    public static void main(String[] args) throws SQLException {
        // connection to PostgreSQL Database
        DBConnection dbConnection = new DBConnection();
        dbConnection.connect();

        // Instantiate template engine
        ThymeleafTemplateEngine tmp = new ThymeleafTemplateEngine();

        // Default server settings
        exception(Exception.class, (e, req, res) -> e.printStackTrace());
        staticFileLocation("/public");
        port(61000);

        // Routes
        get("/newClient", ClientController::newClient);
        post("/review/:APIKey/:productName/:ratings", ReviewController::newReview);
        get("/changeStatus/:APIKey/:reviewKey/:status", ReviewController::changeStatus);
        get("/reviewFromClient/:APIKey", ReviewController::getAllReviewFromClient);
        get("/allReviewOfProduct/:APIKey/:productName", ReviewController::getAllReviewOfProduct);
        get("/", RegistrationPageController::renderRegistrationPage, tmp);

        get("/success",  (req, res) -> "Successful registration");
        get("/newStatus", (req, res) -> "New status of review has been set");
    }
}
