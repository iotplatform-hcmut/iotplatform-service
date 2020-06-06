package com.hcmut.iotplatformservice.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class BaseSensorEntity extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String id;
    private int timeStamp;
    private boolean state;
    private int value;

    public BaseSensorEntity(String id, int value) {
        this.id = id;
        this.value = value;
    }
}