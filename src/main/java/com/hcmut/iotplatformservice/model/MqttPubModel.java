package com.hcmut.iotplatformservice.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

public class MqttPubModel {
    private static final Logger _logger = Logger.getLogger(MqttPubModel.class);
    private IMqttClient publisher;

    private MqttConnectOptions options;

    private MqttPubModel() {
        BasicConfigurator.configure();
        String publisherId = UUID.randomUUID().toString();
        try {
            publisher = new MqttClient("tcp://13.76.250.158", publisherId);
            options = new MqttConnectOptions();
            options.setUserName("BKvm2");
            options.setPassword("Hcmut_CSE_2020".toCharArray());
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            publisher.connect(options);
        } catch (Exception e) {
            _logger.error(e.getMessage(), e);
        }
    }

    private static class LazyHolder {
        static final MqttPubModel _INSTANCE = new MqttPubModel();
    }

    public static MqttPubModel getInstance() {

        return LazyHolder._INSTANCE;
    }

    public static void main(String[] args) {
        try {
            getInstance().publish("device_id_test", true, java.time.LocalDateTime.now().getMinute());
        } catch (Exception e) {
            _logger.error(e.getMessage(), e);
        }

    }

    private MqttMessage getMessage(String id, boolean state, int value) {
        List<JsonObject> res = new ArrayList<>();

        JsonArray values = new JsonArray();
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

    public void publish(String id, boolean state, int value) throws MqttException, MqttPersistenceException {
        publisher.publish("Topic/Speaker", getMessage(id, state, value));
    }

}