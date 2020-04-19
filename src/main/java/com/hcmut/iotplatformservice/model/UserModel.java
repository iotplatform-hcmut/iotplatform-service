package com.hcmut.iotplatformservice.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hcmut.iotplatformservice.database.ConnectionPool;

// Template model
public class UserModel {
    public String addUser(String name, String username, String password, String email, String phone, int birthday) {
        int count = 0; JsonObject message = new JsonObject();

        try (Connection connection = ConnectionPool.getConnection();
                Statement statement = connection.createStatement();) {
            String query = "INSERT INTO `user` (`name`, `username`, `password`, `email`, "
                    + "`phone`, `birthday`) VALUES ('%s', '%s', '%s', '%s', '%s', %s)";
            query = String.format(query, name, username, password, email, phone, birthday);
            count = statement.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(count == 0) message.addProperty("status", "error");
        else message.addProperty("status", "ok");

        return message.toString();
    }

    public String getAllUser() {
        List<JsonObject> listUser = new ArrayList<>();

        try (Connection connection = ConnectionPool.getConnection();
                Statement statement = connection.createStatement();) {
            ResultSet resultSet = statement.executeQuery("SELECT * from user");

            while (resultSet.next()) {
                JsonObject json = new JsonObject();
                json.addProperty("key", resultSet.getInt("id"));
                json.addProperty("name", resultSet.getString("name"));
                json.addProperty("username", resultSet.getString("username"));
                json.addProperty("email", resultSet.getString("email"));
                json.addProperty("phone", resultSet.getString("phone"));
                json.addProperty("birthday", resultSet.getInt("birthday"));
                listUser.add(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String message = new Gson().toJson(listUser);

        return message;
    }

    public String getUserById(int id) {
        JsonObject json = new JsonObject();

        try (Connection connection = ConnectionPool.getConnection();
                Statement statement = connection.createStatement();) {
            String query = "SELECT * from user WHERE id = '%s'";
            query = String.format(query, id);
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                json.addProperty("key", resultSet.getInt("id"));
                json.addProperty("name", resultSet.getString("name"));
                json.addProperty("username", resultSet.getString("username"));
                json.addProperty("email", resultSet.getString("email"));
                json.addProperty("phone", resultSet.getString("phone"));
                json.addProperty("birthday", resultSet.getInt("birthday"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json.toString();
    }

    public String deleteUser(int id) {
        int count = 0; JsonObject message = new JsonObject();

        try (Connection connection = ConnectionPool.getConnection();
                Statement statement = connection.createStatement();) {
            String query = "DELETE FROM user WHERE id = %s";
            query = String.format(query, id);
            count = statement.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(count == 0) message.addProperty("status", "error");
        else message.addProperty("status", "ok");

        return message.toString();
    }

    private UserModel() {

    }

    private static class LazyHolder {
        public static final UserModel _INSTANCE = new UserModel();
    }

    public static UserModel getInstance() {
        return LazyHolder._INSTANCE;
    }
}