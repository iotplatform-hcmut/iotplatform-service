package com.hcmut.iotplatformservice.model;

import com.hcmut.iotplatformservice.entity.*;

import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hcmut.iotplatformservice.database.ConnectionPool;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

// Template model
public class HumidityModel {

    private static ConnectionPool _dbPool = new ConnectionPool();
    private static final Logger _logger = Logger.getLogger(HumidityModel.class);

    public String getValueByDeviceId(String device_id, int limit) {
        JsonArray listValue = new JsonArray();

        String query = "SELECT value FROM humidity WHERE device_id=? LIMIT ?";
        Object[] arrParam = new Object[] { device_id, limit };

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

    public static String getAll(String[] ids, int startTime, int endTime, int state, int min, int max, int limit) {

        String arrId[] = ids;
        List<String> listId = new ArrayList<>();

        if (ids[0].equals("all")) {
            String queryGetId = "SELECT DISTINCT id FROM humidity";
            _dbPool.execute(queryGetId, null, rs -> {
                try {
                    listId.add(rs.getString("id"));
                } catch (SQLException ex) {
                    _logger.info(ex.getMessage(), ex);
                }
            });
            arrId = new String[listId.size()];
            arrId = listId.toArray(arrId);
        }

        Object[] arrParam = new Object[] { ids, startTime, endTime, min, max, limit };
        String query = "SELECT * FROM humidity WHERE ";
        query += "id=? ";
        query += "AND timestamp > ? AND timestamp < ? ";
        if (state > -1) {
            query += "AND state=? ";
            arrParam = new Object[] { ids, startTime, endTime, state, min, max, limit };
        }
        query += "AND value>? AND value<? ";
        query += "LIMIT ?";

        List<Sensor> listSensor = new ArrayList<Sensor>();
        for (String id : arrId) {

            arrParam[0] = id;
            Sensor sensor = new Sensor(id);

            _dbPool.execute(query, arrParam, rs -> {
                try {
                    sensor.pushData(rs.getInt("timestamp"), rs.getInt("state"), rs.getInt("value"));
                } catch (SQLException ex) {
                    _logger.info(ex.getMessage(), ex);
                }
            });
      
            if (sensor.getValues().size() > 0)
                listSensor.add(sensor);

        }

        JsonObject json = new JsonObject();

        json.addProperty("startTime", startTime);
        json.addProperty("endTime", endTime);
        json.addProperty("state", state);
        json.addProperty("minValue", min);
        json.addProperty("maxValue", max);
        json.addProperty("limit", limit);

        String jsonData = new Gson().toJson(listSensor);
        JsonArray jsonArrData = new Gson().fromJson(jsonData, JsonArray.class);
        json.add("data", jsonArrData);

        return json.toString();
    }

    public String getAverageMaxMinHumidity(String ids, int startTime, int endTime) {
        JsonArray listValue = new JsonArray();
        float averageHumidity = 0;
        int maxHumidity = 0, minHumidity = 0;

        String query = "SELECT value FROM humidity WHERE device_id=? AND timestamp > ? AND timestamp < ?";
        Object[] arrParam = new Object[] { ids, startTime, endTime };

        _dbPool.execute(query, arrParam, rs -> {
            try {
                listValue.add(rs.getInt("value"));
            } catch (SQLException ex) {
                _logger.info(ex.getMessage(), ex);
            }
        });

        ArrayList<Integer> listValue2ArrayList = new ArrayList<Integer>();
        for (int i = 0; i < listValue.size(); ++i) {
            listValue2ArrayList.add(Integer.parseInt(listValue.get(i).getAsString()));
        }
        for (int value : listValue2ArrayList) {
            averageHumidity += value;
        }
        averageHumidity = averageHumidity / listValue2ArrayList.size();
        minHumidity = Collections.min(listValue2ArrayList);
        maxHumidity = Collections.max(listValue2ArrayList);

        JsonObject json = new JsonObject();
        json.addProperty("name", ids);
        json.addProperty("averageHumidity", averageHumidity);
        json.addProperty("maxHumidity", maxHumidity);
        json.addProperty("minHumidity", minHumidity);

        return json.toString();
    }

    public static void main(String[] args) {
        System.out.println("\n-------------------------\n---------Run time---------");
        String[] ids = { "d0_0", "d1_0" };
        String test = getAll(ids, 0, Integer.MAX_VALUE, -1, 0, 99999, 5);
        System.out.println(test);
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