package com.hcmut.iotplatformservice.controller;

import java.util.List;

import com.hcmut.iotplatformservice.entity.MoisEntity;
import com.hcmut.iotplatformservice.model.MoisModel;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MoisController extends BaseController {

    @GetMapping(value = "api/mois", produces = "application/json")
    public List<MoisEntity> readHumiditySensor(@RequestParam(value = "ids") List<String> ids,
            @RequestParam(value = "limit", required = false, defaultValue = "200") int limit,
            @RequestParam(value = "start", required = false, defaultValue = "0") int start,
            @RequestParam(value = "end", required = false, defaultValue = "2147483647") int end) {

        return MoisModel.getInstance().getValueByDeviceId(ids, limit, start, end);
    }
}