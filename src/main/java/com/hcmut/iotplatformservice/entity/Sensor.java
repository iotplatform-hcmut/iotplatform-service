package com.hcmut.iotplatformservice.entity;

import lombok.Getter;

import java.util.List;
import java.util.ArrayList;

@Getter
public class Sensor extends BaseEntity {

    private static final long serialVersionUID = 2L;

    private String id;
    private List<Integer> timestamps;
    private List<Integer> values;

    public Sensor(String id) {
        this.id = id;
        this.timestamps = new ArrayList<>();
        this.values = new ArrayList<>();
    }

    // Get current timestamp
    public int getTimestamp(){
        return timestamps.get(timestamps.size() - 1);
    }

    // Get current value
    public int getValue(){
        return values.get(values.size() - 1);
    }

    public void push(int time, int value){
        this.timestamps.add(time);
        this.values.add(value);
    }

    // Thành làm nè
    // public void pushValue(){}

    // public int getMaxValue() {
        
    //     return 0;
    // }
}