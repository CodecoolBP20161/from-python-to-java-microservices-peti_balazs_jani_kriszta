package com.codecool.reviewservice.model;

import java.util.UUID;

public class Client {
    String name;
    int id;
    String APIKey;
    String email;

    public Client(String name, String email){
        this.name = name;
        this.email = email;
        APIKey = UUID.randomUUID().toString();
    }

    public Client(String name, String email, String APIKey){
        this.name = name;
        this.email = email;
        this.APIKey = APIKey;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getAPIKey(){
        return APIKey;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString(){
        return String.format("" +
                "Company name: %s\n" +
                "Email address: %s\n" +
                "API Key: %s\n", getName(), getEmail(), getAPIKey());
    }
}
