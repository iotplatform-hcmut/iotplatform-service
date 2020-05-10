package com.hcmut.iotplatformservice.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hcmut.iotplatformservice.database.ConnectionPool;

// Template model
public class HumidityModel {

    public String getValueByDeviceId(String device_id, int limit) {
        JsonObject json = new JsonObject();
        json.addProperty("name", device_id);
        JsonArray listValue = new JsonArray();

        try (Connection connection = ConnectionPool.getConnection();
                Statement statement = connection.createStatement();) {
            String sql = String.format("SELECT value FROM humidity WHERE device_id='%s' LIMIT %d", device_id, limit);
            ResultSet resultSet = statement.executeQuery(sql);
            
            while (resultSet.next()) listValue.add(resultSet.getInt("value"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        json.add("data", listValue);

        return json.toString();
    }

    private HumidityModel() {

    }

    private static class LazyHolder {
        public static final HumidityModel _INSTANCE = new HumidityModel();
    }

    public static HumidityModel getInstance() {
        return LazyHolder._INSTANCE;
    }
}