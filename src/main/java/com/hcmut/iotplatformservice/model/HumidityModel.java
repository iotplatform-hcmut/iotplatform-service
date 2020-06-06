package com.hcmut.iotplatformservice.model;

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

    public String getAll(String ids, int startTime, int endTime, int state, int min, int max, int limit) {
        
        String arrId[] = null;
        List<String> list = new ArrayList<>();
        if (ids.equals("all")){
            String queryGetId = "SELECT DISTINCT device_id FROM humidity";
            _dbPool.execute(queryGetId, null, rs -> {
                try {
                    list.add(rs.getString("device_id"));
                } catch (SQLException ex) {
                    _logger.info(ex.getMessage(), ex);
                }
            });
            arrId = new String[list.size()];
            arrId = list.toArray(arrId);
        }
        else{
            arrId = ids.split(",");
        }

        JsonArray jsonArrData = new JsonArray();
        JsonObject json = new JsonObject();

        json.addProperty("startTime", String.valueOf(startTime));
        json.addProperty("endTime", String.valueOf(endTime));
        json.addProperty("state", String.valueOf(state));
        json.addProperty("minValue", String.valueOf(min));
        json.addProperty("maxValue", String.valueOf(max));
        json.addProperty("limit", String.valueOf(limit));
        
        Object[] arrParam = new Object[] {ids, startTime, endTime, min, max, limit};
        String query = "SELECT * FROM humidity WHERE ";
            query += "device_id=? ";
            query += "AND timestamp > ? AND timestamp < ? ";
        if (state > -1){
            query += "AND state=? ";
            arrParam = new Object[] {ids, startTime, endTime, state, min, max, limit};
        }
            query += "AND value>? AND value<? ";
            query += "LIMIT ?";
        
        for (String id:arrId){   
            JsonArray listTime = new JsonArray();
            JsonArray listState = new JsonArray();
            JsonArray listValue = new JsonArray();
            JsonObject jsonObj = new JsonObject();
            arrParam[0] = id;
            _dbPool.execute(query, arrParam, rs -> {
                try {
                    listTime.add(rs.getInt("timestamp"));
                    listState.add(rs.getInt("state"));
                    listValue.add(rs.getInt("value"));
                } catch (SQLException ex) {
                    _logger.info(ex.getMessage(), ex);
                }
            });

            jsonObj.addProperty("id", String.valueOf(id));
            jsonObj.add("time", listTime);
            jsonObj.add("state", listState);
            jsonObj.add("value", listValue);
            jsonArrData.add(jsonObj);
        }
    
        json.add("data",jsonArrData);
        return json.toString();
    }

    public String getAverageMaxMinHumidity(String ids, int startTime, int endTime) {
        JsonArray listValue = new JsonArray();
        float averageHumidity = 0;
        int maxHumidity = 0, minHumidity = 0;

        String query = "SELECT value FROM humidity WHERE device_id=? AND timestamp > ? AND timestamp < ?";
        Object[] arrParam = new Object[] {ids, startTime, endTime};

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
        for (int value: listValue2ArrayList){
            averageHumidity += value;
        }
        averageHumidity = averageHumidity/listValue2ArrayList.size();
        minHumidity = Collections.min(listValue2ArrayList);
        maxHumidity = Collections.max(listValue2ArrayList); 

        JsonObject json = new JsonObject();
        json.addProperty("name", ids);
        json.addProperty("averageHumidity", averageHumidity);
        json.addProperty("maxHumidity", maxHumidity);
        json.addProperty("minHumidity", minHumidity);

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

    public static void main(String[] args) {
        System.out.println("\nRun time");
    }
}