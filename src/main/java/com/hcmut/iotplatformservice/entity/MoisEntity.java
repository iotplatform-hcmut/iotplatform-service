package com.hcmut.iotplatformservice.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
public class MoisEntity {
    private String device_id;
    private String[] values;

    @Setter
    private int timestamp;

    public Object[] getArrObj() {
        Object[] arr = { device_id, timestamp, Integer.parseInt(values[0]) };
        return arr;
    }
}