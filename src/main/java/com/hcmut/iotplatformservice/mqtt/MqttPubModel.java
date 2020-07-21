package com.hcmut.iotplatformservice.mqtt;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hcmut.iotplatformservice.database.ConnectionPool;

import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttPubModel {

    private final Logger _logger;
    private final IMqttClient publisher;
    private final ConnectionPool _dbPool;

    public MqttMessage genMessage(String id, Boolean state, int value) {
        List<JsonObject> res = new ArrayList<>();

        JsonArray values = new JsonArray();
        if (state != null)
            values.add(String.valueOf(state ? 1 : 0));
        values.add(String.valueOf(value));

        JsonObject json = new JsonObject();
        json.addProperty("device_id", id);
        json.add("values", values);
        res.add(json);

        byte[] payload = new Gson().toJson(res).getBytes();
        MqttMessage msg = new MqttMessage(payload);
        msg.setRetained(true);

        return msg;
    }

    public Boolean publish(String ids[], boolean state, int value) {
        try {
            String updateQuery = "UPDATE motor SET state = ? WHERE id = ?";
            String insertQuery = "INSERT INTO history(id, timestamp, value) VALUES(?,?,?)";

            if (MqttConnection.isConnected()) {
                for (String id : ids) {
                    publisher.publish(MqttConnection.TOPIC_SPEAKER, genMessage(id, state, value));
                    if (state) {
                        long unixTime = Instant.now().getEpochSecond();
                        _dbPool.execute(insertQuery, Arrays.asList(id, unixTime, value));
                    }
                    _dbPool.execute(updateQuery, Arrays.asList(state, id));
                }
                return true;
            } else {
                int REASON_CODE_SERVER_CONNECT_ERROR = 32103;
                throw new MqttException(REASON_CODE_SERVER_CONNECT_ERROR);
            }
        } catch (Exception ex) {
            _logger.info(ex.getMessage(), ex);
        }
        return false;
    }

    private MqttPubModel() {
        _logger = Logger.getLogger(this.getClass());
        publisher = MqttConnection.client;
        _dbPool = new ConnectionPool();

    }

    private static class LazyHolder {
        static final MqttPubModel _INSTANCE = new MqttPubModel();
    }

    public static MqttPubModel getInstance() {
        return LazyHolder._INSTANCE;

    }
}