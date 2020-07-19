package com.hcmut.iotplatformservice.controller;

import java.util.List;

import com.google.gson.Gson;
import com.hcmut.iotplatformservice.entity.HistoryEntity;
import com.hcmut.iotplatformservice.entity.MotorEntity;
import com.hcmut.iotplatformservice.model.MotorModel;
import com.hcmut.iotplatformservice.mqtt.MqttPubModel;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MotorController extends BaseController {

    @GetMapping(value = "api/motor", produces = "application/json")
    public String readMotorInfo() {
        List<MotorEntity> lsMotor = MotorModel.getInstance().getAllMotor();
        return new Gson().toJson(lsMotor);
    }

    @GetMapping(value = "api/motor/history", produces = "application/json")
    public String readMotorHistoryInfo() {
        List<HistoryEntity> lsMotorHistory = MotorModel.getInstance().getHistory();
        return new Gson().toJson(lsMotorHistory);
    }

    @PostMapping(value = "api/motor", produces = "application/json")
    public String addMotorInfo(@RequestParam(value = "id") String id, @RequestParam(value = "position") String position,
            @RequestParam(value = "description") String description, @RequestParam(value = "state") Boolean state,
            @RequestParam(value = "relay") Integer relay) {
        Boolean flag = MotorModel.getInstance().addMotor(id, position, description, state, relay);

        if (flag) {
            return SUCCESS_MESSAGE;
        }

        return FAIL_MESSAGE;
    }

    @DeleteMapping(value = "api/motor", produces = "application/json")
    public String deleteMotorInfo(@RequestParam(value = "id") String id) {
        
        Boolean flag = MotorModel.getInstance().deleteMotorById(id);

        if (flag) {
            return SUCCESS_MESSAGE;
        }

        return FAIL_MESSAGE;

    }

    @GetMapping(value = "/api/motor/publish", produces = "application/json")
    public String pushlishDataToMotor(@RequestParam(value = "device_id") String deviceId,
            @RequestParam(value = "state") Boolean state, @RequestParam(value = "value") int value) {

        Boolean flag = MqttPubModel.getInstance().publish(deviceId, state, value);

        if (flag) {
            return SUCCESS_MESSAGE;
        }

        return FAIL_MESSAGE;
    }
}