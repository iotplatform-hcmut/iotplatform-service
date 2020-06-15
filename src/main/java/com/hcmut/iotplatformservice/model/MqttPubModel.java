package com.hcmut.iotplatformservice.model;

import java.util.UUID;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttPubModel {
    private static final Logger _logger = Logger.getLogger(MqttPubModel.class);
    private IMqttClient publisher;
    private MqttConnectOptions options;
    
    private MqttPubModel(){
        BasicConfigurator.configure();
        String publisherId = UUID.randomUUID().toString();
        try{
            publisher = new MqttClient("tcp://13.76.250.158", publisherId);
            options = new MqttConnectOptions();
            options.setUserName("BKvm2");
            options.setPassword("Hcmut_CSE_2020".toCharArray());
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            publisher.connect(options);
        } catch (Exception e) {
            _logger.error(e.getMessage(),e);
        }
    }

    private static class LazyHolder{
        static final MqttPubModel _INSTANCE = new MqttPubModel();
    }

    public static MqttPubModel getInstance(){
        return LazyHolder._INSTANCE;
    }

    public static void main(String[] args) {
        getInstance().publish("device_id_test", true, java.time.LocalDateTime.now().getMinute());
    }

    private MqttMessage getMessage(String id, boolean state, int value){

        JsonArray values = new JsonArray();
        values.add(String.valueOf(state?1:0));
        values.add(String.valueOf(value));
        
        JsonObject json = new JsonObject();
        json.addProperty("device_id",id);
        json.add("values",values);

        byte[] payload = json.toString().getBytes();
        MqttMessage msg = new MqttMessage(payload);
        msg.setRetained(true);
        System.out.println(msg);
        return msg;
    }

    public void publish(String id, boolean state, int value){
        try{
            publisher.publish("Topic/Speaker", getMessage(id, state, value));
        }catch(Exception ex){
            _logger.error(ex.getMessage(), ex);
        }
    }
}