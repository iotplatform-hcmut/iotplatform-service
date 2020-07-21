package com.hcmut.iotplatformservice.controller;

import com.hcmut.iotplatformservice.entity.AnomalyEntity;
import com.hcmut.iotplatformservice.model.AnomalyModel;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnomalyController extends BaseController {
    
    @GetMapping(value = "api/anomaly", produces = "application/json")
    public AnomalyEntity readAnomaly() {

        return AnomalyModel.getInstance().getAnomaly();
    }
}