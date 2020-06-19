package com.hcmut.iotplatformservice.model;

import com.hcmut.iotplatformservice.entity.*;

import java.sql.SQLException;

import com.google.gson.Gson;
import com.hcmut.iotplatformservice.database.ConnectionPool;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

import java.util.List;
import java.util.LinkedList;

// Motor model
public class MotorModel {

    private ConnectionPool _dbPool;
    private static final Logger _logger = Logger.getLogger(MotorModel.class);

    public List<MotorEntity> getAllMotor() {
        List<MotorEntity> lsEntity = new LinkedList<>();

        String query = "SELECT * FROM motor";

        _dbPool.execute(query, rs -> {
            try {
                MotorEntity entity = new MotorEntity();
                entity.setId(rs.getString("id"));
                entity.setPosition(rs.getString("position"));
                entity.setDescription(rs.getString("description"));
                entity.setState(rs.getBoolean("state"));
                entity.setRelay(rs.getInt("relay"));
                lsEntity.add(entity);
            } catch (SQLException ex) {
                _logger.info(ex.getMessage(), ex);
            }
        });

        return lsEntity;
    }

    public void addMotor(String id, String position, String description, Boolean state, Integer relay) {
        String query = "INSERT INTO motor (`id`,`position`,`description`,`state`,`relay`) VALUES (?,?,?,?,?)";

        _dbPool.execute(query, new Object[] { id, position, description, state, relay });
    }

    private static void testGetValueByDeviceId() {
        List<MotorEntity> lsMotor = getInstance().getAllMotor();
        System.out.println(new Gson().toJson(lsMotor));
    }

    private static void testAddMotor() {
        getInstance().addMotor("d2_9", "Tiem banh", "Tuoi rau", false, 10000);
    }

    public static void main(String[] args) {
        testAddMotor();
        testGetValueByDeviceId();
    }

    private MotorModel() {
        BasicConfigurator.configure();
        _dbPool = new ConnectionPool();
    }

    private static class LazyHolder {
        public static final MotorModel _INSTANCE = new MotorModel();
    }

    public static MotorModel getInstance() {
        return LazyHolder._INSTANCE;
    }

}