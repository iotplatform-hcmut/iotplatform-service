package com.hcmut.iotplatformservice.model;

import java.sql.SQLException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hcmut.iotplatformservice.database.ConnectionPool;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

// Template model
public class HumidityModel {

    private ConnectionPool _dbPool = new ConnectionPool();
    private static final Logger _logger = Logger.getLogger(HumidityModel.class);

    public String getValueByDeviceId(String device_id, int limit) {
        JsonArray listValue = new JsonArray();

        String query = "SELECT value FROM humidity WHERE device_id=? LIMIT ?";
        Object[] arrParam = new Object[] {device_id, limit};

        _dbPool.execute(query, arrParam, rs -> {
            try {
                listValue.add(rs.getInt("value"));
            } catch (SQLException ex) {
                _logger.info(ex.getMessage(), ex);
            }
        });

        JsonObject json = new JsonObject();
        json.addProperty("name", device_id);
        json.add("data", listValue);

        return json.toString();
    }

    private HumidityModel() {
        BasicConfigurator.configure();
    }

    private static class LazyHolder {
        public static final HumidityModel _INSTANCE = new HumidityModel();
    }

    public static HumidityModel getInstance() {
        return LazyHolder._INSTANCE;
    }
}