package com.hcmut.iotplatformservice.entity;

import lombok.Getter;

@Getter
public class Sensor {
    private String id;
    private String position;
    private String description;
    private Boolean state;
    private String history;

    
}