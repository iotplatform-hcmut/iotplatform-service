package com.hcmut.iotplatformservice.controller;

import com.google.gson.JsonObject;
import com.hcmut.iotplatformservice.mqtt.MqttPubModel;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MqttPubController {

    // Publish device
    @GetMapping(value = "/api/publish", produces = "application/json")
    public String pushlishDataToMotor(@RequestParam(value = "device_id") String deviceId,
            @RequestParam(value = "state") Boolean state, @RequestParam(value = "value") int value) {
        JsonObject json = new JsonObject();

        try {
            MqttPubModel.getInstance().publish(deviceId, state, value);
            json.addProperty("status", "ok");
        } catch (Exception e) {
            json.addProperty("status", "error");
        }

        return json.toString();
    }
}