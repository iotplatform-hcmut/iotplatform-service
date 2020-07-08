package com.hcmut.iotplatformservice.controller;

import com.google.gson.JsonObject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController extends BaseController {

        @GetMapping(value = "/", produces = "application/json")
        public String welcome() {
                final JsonObject json = new JsonObject();
                json.addProperty("Team", "IoT Platform");
                json.addProperty("Message", "Welcome to IoT Platform API");
                return json.toString();
        }
}