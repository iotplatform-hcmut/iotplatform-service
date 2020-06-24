package com.hcmut.iotplatformservice.mqtt;

import java.util.UUID;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.scheduling.annotation.EnableScheduling;

// @SpringBootApplication
// @EnableScheduling
class testMqtt {

    // add @SpringBootApplication and @EnableScheduling before run
    public static void main(String[] args) {
        SpringApplication.run(testMqtt.class, args);
    }

    @Scheduled(zone = "Asia/Saigon", initialDelay = 1 * 500, fixedRate = 6 * 500)
    private void autoPub() throws MqttPersistenceException, MqttException {
        int value = java.time.LocalDateTime.now().getSecond();
        MqttMessage msg = MqttPubModel.getInstance().getMessage("test_id", null, value);
        MqttConnection.client.publish(MqttConnection._TOPIC_MOIS, msg);
    }
}

@Component
public class MqttConnection {

    private static final Logger _logger = Logger.getLogger(MqttConnection.class);

    public static final String _TOPIC_SPEAKER = "Topic/Speaker";
    public static final String _TOPIC_MOIS = "Topic/Mois";

    public static IMqttClient client;

    private MqttConnection() {
        BasicConfigurator.configure();
        String clientId = UUID.randomUUID().toString();
        try {
            // String host = "tcp://13.76.250.158:1883";
            String myHost = "tcp://iotplatform.xyz:1883";
            client = new MqttClient(myHost, clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            // options.setUserName("BKvm2");
            // options.setPassword("Hcmut_CSE_2020".toCharArray());
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            client.connect(options);
        } catch (Exception e) {
            System.out.println("\n\nServer mqtt not connected");
            _logger.error(e.getMessage(), e);
        }
    };

    public static boolean isConnected() {
        if (client != null) {
            return true;
        }
        return false;
    }

    // private static class LazyHolder {
    // static final MqttConnection _INSTANCE = new MqttConnection();
    // }

    // public static MqttConnection getInstance() {
    // return LazyHolder._INSTANCE;
    // }
}