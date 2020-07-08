package com.hcmut.iotplatformservice.mqtt;

import java.util.UUID;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.stereotype.Component;

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
            _logger.error(e.getMessage(), e);
        }
    };

    public static boolean isConnected() {
        if (client != null) {
            return true;
        }
        return false;
    }

    private static class LazyHolder {
        static final MqttConnection _INSTANCE = new MqttConnection();
    }

    public static MqttConnection getInstance() {
        return LazyHolder._INSTANCE;
    }
}