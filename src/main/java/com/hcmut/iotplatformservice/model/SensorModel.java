package com.hcmut.iotplatformservice.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hcmut.iotplatformservice.database.ConnectionPool;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class SensorModel {
    private static final ConnectionPool _dbPool = new ConnectionPool();
    private static final Logger _logger = Logger.getLogger(SensorModel.class);
    
    private SensorModel(){
        BasicConfigurator.configure();
    }

    private static class LazyHolder{
        static final SensorModel _INSTANCE = new SensorModel();
    }

    public static SensorModel getInstance(){
        return LazyHolder._INSTANCE;
    }

    public String[] getAllIdSenSor(){
        String query = "SELECT id FROM sensor";
        List<String> lsId = new ArrayList<String>();
        _dbPool.execute(query, rs->{
            try{
                lsId.add(rs.getString("id"));
            }catch(SQLException ex){
                _logger.info(ex.getMessage(), ex);
            }
        });
        String[] arrId = new String[lsId.size()];
        return lsId.toArray(arrId);
    }

    public static void main(String[] args) {
        System.out.println(SensorModel.getInstance().getAllIdSenSor().toString());
    }
}