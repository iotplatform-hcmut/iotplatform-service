package com.hcmut.iotplatformservice.controller;

import java.util.List;

import com.google.gson.Gson;
import com.hcmut.iotplatformservice.entity.MotorEntity;
import com.hcmut.iotplatformservice.model.MotorModel;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MotorController {

    // READ
    @GetMapping(value = "api/motor", produces = "application/json")
    public String readMotorInfo() {
        List<MotorEntity> lsMotor = MotorModel.getInstance().getAllMotor();
        return new Gson().toJson(lsMotor);
    }

    // ADD
    @GetMapping(value = "api/addMotor", produces = "application/json")
    public String addMotorInfo(@RequestParam(value = "id") String id, @RequestParam(value = "position") String position,
            @RequestParam(value = "description") String description, @RequestParam(value = "state") Boolean state,
            @RequestParam(value = "relay") Integer relay) {
        MotorModel.getInstance().addMotor(id, position, description, state, relay);

        return "{\"status\":\"ok\"}";
    }
}