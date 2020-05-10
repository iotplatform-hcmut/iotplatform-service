package com.hcmut.iotplatformservice.controller;

import com.hcmut.iotplatformservice.model.HumidityModel;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Humidity controller
@RestController
public class HumidityController {

    // READ
    @GetMapping(value = "api/humidity", produces = "application/json")
    public String readHumiditySensor(@RequestParam(value = "device_id") String device_id,
            @RequestParam(value = "limit") int limit) {
        return HumidityModel.getInstance().getValueByDeviceId(device_id, limit);
    }
}