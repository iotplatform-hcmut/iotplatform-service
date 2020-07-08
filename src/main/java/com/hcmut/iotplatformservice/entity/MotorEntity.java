package com.hcmut.iotplatformservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MotorEntity extends BaseEntity {
    private static final long serialVersionUID = 1721969666584007239L;
    String id;
    String position;
    String description;
    Boolean state;
    Integer relay;
}