package com.hcmut.iotplatformservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SensorEntity extends BaseEntity {
    private static final long serialVersionUID = 5034810906970598721L;
    private String id;
    private String position;
    private String description;
    private Boolean state;
    private int relay;
    private String history;
}