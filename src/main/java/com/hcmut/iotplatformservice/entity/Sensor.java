package com.hcmut.iotplatformservice.entity;

import lombok.Getter;

import java.util.List;
import java.util.ArrayList;

@Getter
public class Sensor extends BaseSensorEntity {

    private static final long serialVersionUID = 2L;

    private List<Integer> timestamps;
    private List<Integer> states;
    private List<Integer> values;

    public Sensor(String id) {
        this.id = id;
        this.timestamps = new ArrayList<>();
        this.states = new ArrayList<>();
        this.values = new ArrayList<>();
    }

    @Override
    public int getTimestamp(){
        return timestamps.get(timestamps.size() - 1);
    }

    @Override
    public int getState(){
        return states.get(states.size() - 1);
    }

    @Override
    public int getValue(){
        return values.get(values.size() - 1);
    }

    public void pushData(int time, int state, int value){
        this.timestamps.add(time);
        this.states.add(state);
        this.values.add(value);
    }

    public int getMaxValue() {
        // Thành làm nè
        return 0;
    }
}