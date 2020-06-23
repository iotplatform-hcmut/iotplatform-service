package com.hcmut.iotplatformservice.model;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@SpringBootApplication
public class MqttSubModel {
    

    // private MqttSubModel(){};

    private static class LazyHolder {
        static final MqttSubModel _INSTANCE = new MqttSubModel();
    }

    public static MqttSubModel getInstance() {
        return LazyHolder._INSTANCE;
    }

    // Comment constructor (line 17) va them annotation @SpringBootApplication (line12) truoc khi chay 
    public static void main(String[] args) {

        // Comment constructor (line 17) va them annotation @SpringBootApplication (line12) truoc khi chay 
        SpringApplication.run(MqttSubModel.class, args);
    }

    @Scheduled(zone="Asia/Saigon", initialDelay = 1 * 500, fixedRate = 1 * 500)
    public void writeCurrentTime() throws InterruptedException {
         
        System.out.println("Now is: "+ java.time.LocalDateTime.now().getSecond());
        // Thread.sleep(500L);
    }
}

