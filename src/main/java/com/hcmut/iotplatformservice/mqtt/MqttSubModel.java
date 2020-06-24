package com.hcmut.iotplatformservice.mqtt;

import java.util.LinkedList;
import java.util.Queue;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.hcmut.iotplatformservice.database.ConnectionPool;
import com.hcmut.iotplatformservice.entity.MoisEntity;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Component;

@Component
public class MqttSubModel {

    private static final Logger _logger = Logger.getLogger(MqttSubModel.class);
    private final IMqttClient subscriber = MqttConnection.client;
    private ConnectionPool _dbPool;
    private Queue<MoisEntity> queue = new LinkedList<>();

    private MqttSubModel() {
        BasicConfigurator.configure();
        _dbPool = new ConnectionPool();
        try {
            subscriber.subscribe(MqttConnection._TOPIC_MOIS, (topic, msg) -> {
                System.out.println(topic + ":" + msg);
                ExtractMessage(msg.toString());
                InsertDB();
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    };

    private void ExtractMessage(String msg) {
        try {
            JsonArray jsonArrData = new Gson().fromJson(msg, JsonArray.class);
            for (JsonElement jsonElement : jsonArrData) {
                MoisEntity moi = new Gson().fromJson(jsonElement, MoisEntity.class);
                moi.setTimestamp((int) (System.currentTimeMillis() / 1000));
                queue.add(moi);
            }
        } catch (Exception e) {
            _logger.info(e.getMessage(), e);
        }
    }

    private void InsertDB() {
        String query = "INSERT INTO humidity(id, timestamp, value) VALUES(?, ?, ?)";
        while (!queue.isEmpty()) {
            Object[] arr = queue.remove().getArrObj();
            try {
                _dbPool.execute(query, arr);
            } catch (Exception e) {
                System.out.println("\n Error insert database");
                _logger.info(e.getMessage(), e);
            }
        }
    }
}