package com.hcmut.iotplatformservice.controller;

import com.google.gson.JsonObject;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MqttPubController {

    // Publish device
	@PutMapping(value = "/api/publish", produces = "application/int")
	public int welcome() {
        JsonObject json = new JsonObject();
        json.addProperty("Team", "IoT Platform");
        json.addProperty("Message", "Welcome to IoT Platform API");
        return 1;
	}
}