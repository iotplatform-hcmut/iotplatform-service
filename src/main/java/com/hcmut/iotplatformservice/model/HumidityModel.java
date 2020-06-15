package com.hcmut.iotplatformservice.model;

import com.hcmut.iotplatformservice.entity.*;

import java.sql.SQLException;

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

    private ConnectionPool _dbPool;
    private static final Logger _logger = Logger.getLogger(UserModel.class);

    public String getValueByDeviceId(String device_id, int limit) {
        JsonArray listValue = new JsonArray();

        String query = "SELECT value FROM humidity WHERE id=? LIMIT ?";
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

    private String[] getAllId() {
        List<String> listId = new ArrayList<>();
        String queryGetId = "SELECT DISTINCT id FROM humidity";
        _dbPool.execute(queryGetId, null, rs -> {
            try {
                listId.add(rs.getString("id"));
            } catch (SQLException ex) {
                _logger.info(ex.getMessage(), ex);
            }
        });
        String[] arrId = new String[listId.size()];
        return listId.toArray(arrId);
    }

    public List<Sensor> getAll(String[] ids, int startTime, int endTime, int min, int max, int limit) {

        String[] arrId = ids;
        if (ids[0].equals("all")) {
            arrId = getAllId();
        }

        List<Object> arr = new ArrayList<>();
        arr.add(ids);
        arr.add(startTime);
        arr.add(endTime);

        String query = "SELECT * FROM humidity WHERE ";
        query += "id=? ";
        query += "AND timestamp > ? AND timestamp < ? ";
        if (min > 0) {
            query += "AND value>? AND value<? ";
            arr.add(min);
            arr.add(max);
        }
        if (limit > 0) {
            query += "LIMIT ?";
            arr.add(limit);
        }

        List<Sensor> listSensor = new ArrayList<Sensor>();
        for (String id : arrId) {

            arr.set(0, id);
            Sensor sensor = new Sensor(id);

            _dbPool.execute(query, arr.toArray(), rs -> {
                try {
                    sensor.push(rs.getInt("timestamp"), rs.getInt("value"));
                } catch (SQLException ex) {
                    _logger.info(ex.getMessage(), ex);
                }
            });

            if (sensor.getValues().size() > 0)
                listSensor.add(sensor);
        }

        return listSensor;
    }

    public List<AverageMaxMinHumidity> getAverageMaxMinHumidity(String[] ids, int startTime, int endTime) {

        List<AverageMaxMinHumidity> listAverageMaxMinHumidity = new ArrayList<AverageMaxMinHumidity>();
        List<Object> arr = new ArrayList<>();
        arr.add(0);
        arr.add(startTime);
        arr.add(endTime);

        String query = "SELECT value FROM humidity WHERE id=? AND timestamp > ? AND timestamp < ? ";

        String[] arrId = ids;
        if (ids[0].equals("all")) {
            arrId = getAllId();
        }

        for (String id : arrId) {

            AverageMaxMinHumidity AMMHumidity = new AverageMaxMinHumidity(id, startTime, endTime);
            ArrayList<Integer> totalValueList = new ArrayList<Integer>();
            float averageHumidity = 0;

            arr.set(0, id);
            _dbPool.execute(query, arr.toArray(), rs -> {
                try {
                    totalValueList.add(rs.getInt("value"));
                } catch (SQLException ex) {
                    _logger.info(ex.getMessage(), ex);
                }
            });

            if (Collections.max(totalValueList) > 0) {
                for (int value : totalValueList) {
                    averageHumidity += value;
                }
                averageHumidity = averageHumidity / totalValueList.size();
                AMMHumidity.setAverageMaxMin(averageHumidity, Collections.max(totalValueList),
                        Collections.min(totalValueList));
                listAverageMaxMinHumidity.add(AMMHumidity);
            }
        }

        return listAverageMaxMinHumidity;
    }

    private static void testGetValueByDeviceId() {
        String id = "d0_0";
        System.out.println(getInstance().getValueByDeviceId(id, 5));
    }

    private static void testGetAll() {
        String[] ids = { "d0_0", "d1_0" };
        List<Sensor> test = getInstance().getAll(ids, 0, Integer.MAX_VALUE, 0, 99999, 5);
        System.out.println(test.toString());
    }

    private static void testGetAverageMaxMinHumidity() {
        String[] ids = { "d1_0", "d1_1" };
        List<AverageMaxMinHumidity> test = getInstance().getAverageMaxMinHumidity(ids, 0, 2000000000);
        System.out.println(test.toString());
    }

    public static void main(String[] args) {
        testGetAll();
        testGetValueByDeviceId();
        testGetAverageMaxMinHumidity();
    }

    private HumidityModel() {
        BasicConfigurator.configure();
        _dbPool = new ConnectionPool();
    }

    private static class LazyHolder {
        public static final HumidityModel _INSTANCE = new HumidityModel();
    }

    public static HumidityModel getInstance() {
        return LazyHolder._INSTANCE;
    }

}