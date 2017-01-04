package com.codecool.reviewservice.dao;

import com.codecool.reviewservice.model.Client;

public interface ClientDao {
    void add(Client clientModel);
    void remove(String APIKey);
    Client getById(int id);
    Client getByAPIKey(String APIKey);
}
