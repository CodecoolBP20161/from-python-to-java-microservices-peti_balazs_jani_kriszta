package com.codecool.reviewservice.controller;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;


public class RegistrationPageController {
    public static ModelAndView renderRegistrationPage(Request request, Response response) {
        Map params = new HashMap<>();
        return new ModelAndView(params, "registration");
    }
    public static ModelAndView renderSuccessPage(Request request, Response response) {
        Map params = new HashMap<>();
        return new ModelAndView(params, "success");
    }
}


