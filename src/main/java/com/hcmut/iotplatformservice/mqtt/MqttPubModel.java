package com.hcmut.iotplatformservice.mqtt;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttPubModel {

    private final Logger _logger;
    private final IMqttClient publisher;

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

    public Boolean publish(String id, boolean state, int value) {
        try {
            if (MqttConnection.isConnected()) {
                publisher.publish(MqttConnection._TOPIC_SPEAKER, genMessage(id, state, value));
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
    }

    private static class LazyHolder {
        static final MqttPubModel _INSTANCE = new MqttPubModel();
    }

    public static MqttPubModel getInstance() {
        return LazyHolder._INSTANCE;

    }
}