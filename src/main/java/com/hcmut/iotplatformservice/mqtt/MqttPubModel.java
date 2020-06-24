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
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

public class MqttPubModel {

    private static final Logger _logger = Logger.getLogger(MqttPubModel.class);
    private final IMqttClient publisher = MqttConnection.client;

    private MqttPubModel() {
    }

    private static class LazyHolder {
        static final MqttPubModel _INSTANCE = new MqttPubModel();
    }

    public static MqttPubModel getInstance() {
        return LazyHolder._INSTANCE;
    }

    public MqttMessage getMessage(String id, Boolean state, int value) {
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

        System.out.println("\npublish message : " + msg.toString());

        return msg;
    }

    public void publish(String id, boolean state, int value) throws MqttException, MqttPersistenceException {
        try {
            if (MqttConnection.isConnected()) {
                publisher.publish(MqttConnection._TOPIC_SPEAKER, getMessage(id, state, value));
            } else {
                System.out.println("Mqtt not connect");
                int REASON_CODE_SERVER_CONNECT_ERROR = 32103;
                throw new MqttException(REASON_CODE_SERVER_CONNECT_ERROR);
            }
        } catch (MqttException ex) {
            _logger.info(ex.getMessage(), ex);
        }

    }

}