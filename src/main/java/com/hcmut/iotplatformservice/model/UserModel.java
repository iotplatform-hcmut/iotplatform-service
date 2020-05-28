package com.hcmut.iotplatformservice.model;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hcmut.iotplatformservice.database.ConnectionPool;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

// Template model
public class UserModel {

    private ConnectionPool _dbPool = new ConnectionPool();
    private static final Logger _logger = Logger.getLogger(UserModel.class);

    public String addUser(String name, String username, String password, String email, String phone, int birthday) {
        String query = "INSERT INTO user (name, username, password, email, phone, birthday) VALUES (?,?,?,?,?,?)";
        Object[] arrParam = new Object[] {name, username, password, email, phone, birthday};

        JsonObject message = new JsonObject();
        int count = _dbPool.execute(query, arrParam);

        if(count == 0) message.addProperty("status", "error");
        else message.addProperty("status", "ok");

        return message.toString();
    }

    public String getUserById(int[] ids) {
        List<JsonObject> lsUser = new LinkedList<>();        

        String query = String.format(
            "SELECT * from user WHERE id in (%s)", 
            ConnectionPool.genQuestion(ids.length));

        Object[] arrParam = new Object[ids.length];
        for(int i = 0; i < ids.length; ++i) arrParam[i] = ids[i];

        _dbPool.execute(query, arrParam, rs -> {
            try {
                JsonObject json = new JsonObject();
                json.addProperty("key", rs.getInt("id"));
                json.addProperty("name", rs.getString("name"));
                json.addProperty("username", rs.getString("username"));
                json.addProperty("email", rs.getString("email"));
                json.addProperty("phone", rs.getString("phone"));
                json.addProperty("birthday", rs.getInt("birthday"));
                lsUser.add(json);
            } catch (SQLException ex) {
                _logger.info(ex.getMessage(), ex);
            }
        });

        return new Gson().toJson(lsUser);
    }

    public String deleteUser(int[] ids) {
        String query = String.format(
            "DELETE FROM user WHERE id in (%s)", 
            ConnectionPool.genQuestion(ids.length));

        Object[] arrParam = new Object[ids.length];
        for(int i = 0; i < ids.length; ++i) arrParam[i] = ids[i];

        JsonObject message = new JsonObject();

        int count = _dbPool.execute(query, arrParam);


        if(count == 0) message.addProperty("status", "error");
        else message.addProperty("status", "ok");

        return message.toString();
    }

    private UserModel() {
        BasicConfigurator.configure();
    }

    private static class LazyHolder {
        public static final UserModel _INSTANCE = new UserModel();
    }

    public static UserModel getInstance() {
        return LazyHolder._INSTANCE;
    }
}