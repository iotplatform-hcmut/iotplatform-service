package com.hcmut.iotplatformservice.entity;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnomalyEntity extends BaseEntity{

    private static final long serialVersionUID = 1L;
    private List<Float> origin;
    private List<Float> season;
    private List<Float> trend;
    private List<Float> residual;

}