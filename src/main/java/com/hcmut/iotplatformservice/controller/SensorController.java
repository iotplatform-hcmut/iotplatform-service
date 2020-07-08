package com.hcmut.iotplatformservice.controller;

import java.util.List;

import com.hcmut.iotplatformservice.entity.SensorEntity;
import com.hcmut.iotplatformservice.model.SensorModel;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SensorController extends BaseController {

    @GetMapping(value = "api/sensor", produces = "application/json")
    public List<SensorEntity> readSensorInfo() {

        List<SensorEntity> lsSensor = SensorModel.getInstance().getAllSensor();

        return lsSensor;
    }

    @PostMapping(value = "api/sensor", produces = "application/json")
    public String addMotorInfo(
            @RequestParam(value = "id") String id,
            @RequestParam(value = "position") String position,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "state") Boolean state,
            @RequestParam(value = "relay") Integer relay,
            @RequestParam(value = "relay") String history) {

        Boolean flag = SensorModel.getInstance().addSensor(id, position, description, state, relay, history);

        if (flag) {
            return SUCCESS_MESSAGE;
        }

        return FAIL_MESSAGE;
    }

    @DeleteMapping(value = "api/sensor", produces = "application/json")
    public String deleteMotorInfo(@RequestParam(value = "id") String id) {

        Boolean flag = SensorModel.getInstance().deleteSensorById(id);

        if (flag) {
            return SUCCESS_MESSAGE;
        }

        return FAIL_MESSAGE;
    }
}