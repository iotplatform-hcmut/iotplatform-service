package com.hcmut.iotplatformservice.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class BaseSensorEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    protected String id;
    private int timestamp;
    private int state;
    private int value;
}