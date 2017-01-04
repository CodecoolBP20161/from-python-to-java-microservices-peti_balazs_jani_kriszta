package com.codecool.reviewservice.dao.implementation;

import com.codecool.reviewservice.dao.ClientDao;
import com.codecool.reviewservice.dao.Connection.DBConnection;
import com.codecool.reviewservice.model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ClientDaoJdbc implements ClientDao {
    private static final Logger logger = LoggerFactory.getLogger(ClientDaoJdbc.class);
    private DBConnection connection = new DBConnection();
    private static ClientDaoJdbc instance = null;
    private String sql;

    public static ClientDaoJdbc getInstance(){
        if(instance == null){
            instance = new ClientDaoJdbc();
            logger.info("New ClientDaoJdbc instance created: {}", instance);
        }
        return instance;
    }

    @Override
    public void add(Client clientModel) {
        String APIKey = clientModel.getAPIKey();
        String name = clientModel.getName();
        String email = clientModel.getEmail();

        sql = "INSERT INTO client (api_key, name, email)" +
                "VALUES(" + APIKey + ", '" + name + "', '" + email + "');";
        logger.debug("Saving to database: {}", clientModel);
        executeQuery(sql);
    }

    @Override
    public void remove(String APIKey) {
        sql = "DELETE FROM client WHERE APIKey='" + APIKey + "';";
        logger.debug("Deleting client with API key {}", APIKey);
        executeQuery(sql);
    }

    @Override
    public Client getById(int id) {
        sql = "SELECT * FROM client WHERE id='" + id + "';";
        logger.debug("Selecting client with id {}", id);
        return createClientModel(sql);
    }

    @Override
    public Client getByAPIKey(String APIKey) {
        sql = "SELECT * FROM client WHERE id='" + APIKey + "';";
        logger.debug("Selecting client with API key {}", APIKey);
        return createClientModel(sql);
    }

    private Client createClientModel(String sql){
        try (Connection conn = connection.connect();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(sql)){
            if (rs.next()){
                Client client = new Client(rs.getString("name"), rs.getString("email"), rs.getString("api_key"));
                client.setId(rs.getInt("id"));
                return client;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void executeQuery(String sql){
        connection.executeQuery(sql);
    }

}
