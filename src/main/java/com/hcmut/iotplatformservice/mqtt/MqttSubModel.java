package com.hcmut.iotplatformservice.mqtt;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class MqttSubModel {

    private static final Logger _logger = Logger.getLogger(MqttSubModel.class);
    private final IMqttClient subscriber = MqttConnection.client;

    private MqttSubModel() {
        BasicConfigurator.configure();
        try {
            subscriber.subscribe(MqttConnection._TOPIC_SPEAKER, (topic, msg) -> { 
                System.out.println(topic + ":" + msg);
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    };

    @Scheduled(zone="Asia/Saigon", initialDelay = 1 * 500, fixedRate = 4 * 500)
    private void autoPub() {
        try {
            MqttPubModel.getInstance().publish("deviceId", true, java.time.LocalDateTime.now().getSecond());
        } catch (Exception e) {
            System.out.println("err pub");
        }
    }
}