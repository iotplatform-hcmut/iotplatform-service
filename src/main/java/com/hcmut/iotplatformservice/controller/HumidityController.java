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


    @GetMapping(value = "api/humidities", produces = "application/json")
    public String readHumiditiesSensor(
        @RequestParam(value = "ids", required = false, defaultValue = "all") String ids,
        @RequestParam(value = "startTime", required = false, defaultValue = "0") int startTime,
        @RequestParam(value = "endTime", required = false, defaultValue = "99999") int endTime,
        @RequestParam(value = "state", required = false, defaultValue = "-1") int state,
        @RequestParam(value = "min", required = false, defaultValue = "0") int min,
        @RequestParam(value = "max", required = false, defaultValue = "300") int max,
        @RequestParam(value = "limit", required = false, defaultValue ="5") int limit
        ) 
    {
        return HumidityModel.getInstance().getAll(ids, startTime, endTime, state, min, max, limit);
    }


    @GetMapping(value = "api/humidity_averagemaxmin", produces = "application/json")
    public String readHumiditiesSensor(
        @RequestParam(value = "ids", required = false, defaultValue = "all") String ids,
        @RequestParam(value = "startTime", required = false, defaultValue = "0") int startTime,
        @RequestParam(value = "endTime", required = false, defaultValue = "99999") int endTime
        ) 
    {
        return HumidityModel.getInstance().getAverageMaxMinHumidity(ids, startTime, endTime);
    }