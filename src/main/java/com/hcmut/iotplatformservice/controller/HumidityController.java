package com.hcmut.iotplatformservice.controller;

import java.util.List;

import com.google.gson.Gson;
import com.hcmut.iotplatformservice.entity.AverageMaxMinHumidity;
import com.hcmut.iotplatformservice.entity.SensorData;
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
            @RequestParam(value = "ids", required = false, defaultValue = "all") String[] ids,
            @RequestParam(value = "startTime", required = false, defaultValue = "0") int startTime,
            @RequestParam(value = "endTime", required = false, defaultValue = "2000000000") int endTime,
            @RequestParam(value = "min", required = false, defaultValue = "0") int min,
            @RequestParam(value = "max", required = false, defaultValue = "0") int max,
            @RequestParam(value = "limit", required = false, defaultValue = "0") int limit) {

        List<SensorData> listSensor = HumidityModel.getInstance().getAll(ids, startTime, endTime, min, max, limit);
        String jsonData = new Gson().toJson(listSensor);

        return jsonData;
    }

    @GetMapping(value = "api/humidity_averageMaxMin", produces = "application/json")
    public String readAverageMaxMinSensor(
            @RequestParam(value = "ids", required = false, defaultValue = "all") String[] ids,
            @RequestParam(value = "startTime", required = false, defaultValue = "0") int startTime,
            @RequestParam(value = "endTime", required = false, defaultValue = "2000000000") int endTime) {


        List<AverageMaxMinHumidity> listAverageMaxMinHumidity = HumidityModel.getInstance()
                .getAverageMaxMinHumidity(ids, startTime, endTime);

        String jsonData = new Gson().toJson(listAverageMaxMinHumidity);

        return jsonData;
    }
}