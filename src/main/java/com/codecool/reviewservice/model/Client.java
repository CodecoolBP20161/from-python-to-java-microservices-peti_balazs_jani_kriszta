package com.codecool.reviewservice.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.lang.System.nanoTime;

public class Client {
    String name;
    int id;
    String APIKey;
    String email;

    public Client(String name, String email){
        this.name = name;
        this.email = email;
        APIKey = createAPIKey();
    }

    public Client(String name, String email, String APIKey){
        this.name = name;
        this.email = email;
        this.APIKey = APIKey;
    }

    private String createAPIKey(){
        long timestamp = nanoTime();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update((byte) timestamp);
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }

        return sb.toString();
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
}
