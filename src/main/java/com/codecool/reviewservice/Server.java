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
        port(8888);

        // Routes
        get("/newClient", (request, response) -> ClientController.newClient(request, response));
        get("/review/:APIKey/:productName/:comment/:ratings", (request, response) -> ReviewController.newReview(request, response));
        get("/changeStatus/:APIKey/:reviewKey/:status", (request, response) -> ReviewController.changeStatus(request, response));
        get("/reviewFromClient/:APIKey", (request, response) -> ReviewController.getAllReviewFromClient(request, response));
        get("/allReviewOfProduct/:APIKey/:ProductName", (request, response) -> ReviewController.getAllReviewOfProduct(request, response));
        get("/", RegistrationPageController::renderRegistrationPage, tmp);
        get("/success",  (req, res) -> "Successful registration");
    }
}
