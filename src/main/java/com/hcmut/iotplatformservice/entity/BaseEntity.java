package com.hcmut.iotplatformservice.entity;

import java.io.Serializable;
import com.google.gson.Gson;

public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }

    public String toJson(){
        return new Gson().toJson(this);
    }
}