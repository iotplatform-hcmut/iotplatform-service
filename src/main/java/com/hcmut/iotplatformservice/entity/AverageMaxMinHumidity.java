package com.hcmut.iotplatformservice.entity;

import lombok.Getter;


@Getter
public class AverageMaxMinHumidity extends BaseEntity {

    private static final long serialVersionUID = 2L;

    private String id;
    private int startTime;
    private int endTime;
    private float Average = 0;
    private int Max = 0;
    private int Min = 0;

    public AverageMaxMinHumidity(String id, int startTime, int endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void setAverageMaxMin(float Average,int Max, int Min){
        this.Average = Average;
        this.Max = Max;
        this.Min = Min;
    }

}