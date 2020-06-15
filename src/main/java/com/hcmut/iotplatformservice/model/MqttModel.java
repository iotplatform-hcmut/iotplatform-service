package com.hcmut.iotplatformservice.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hcmut.iotplatformservice.database.ConnectionPool;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttModel {
    private static final Logger _logger = Logger.getLogger(MqttModel.class);
    private IMqttClient publisher;
    
    private MqttModel(){
        BasicConfigurator.configure();
        String publisherId = UUID.randomUUID().toString();
        try{
            publisher = new MqttClient("tcp://iotplatform.xyz:1883",publisherId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            publisher.connect(options);
        } catch (Exception e) {
            _logger.error(e.getMessage(),e);
        }
    }

    private static class LazyHolder{
        static final MqttModel _INSTANCE = new MqttModel();
    }

    public static MqttModel getInstance(){
        return LazyHolder._INSTANCE;
    }

    public static void main(String[] args) {
        getInstance().send();
    }

    public void send(){

        JsonObject json = new JsonObject();
        json.addProperty("device_id","Speaker");
        JsonArray values = new JsonArray();
        values.add("0");
        values.add("100");
        json.add("values",values);

        byte[] payload = json.toString().getBytes();
        MqttMessage msg = new MqttMessage(payload);

        msg.setQos(0);
        msg.setRetained(true);
        try{
            publisher.publish("TOPIC", msg);
            System.out.println("hh");
        }catch(Exception ex){
            _logger.error(ex.getMessage(), ex);
        }
    }


}