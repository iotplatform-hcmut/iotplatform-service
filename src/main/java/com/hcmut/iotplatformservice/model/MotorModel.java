package com.hcmut.iotplatformservice.model;

import com.hcmut.iotplatformservice.entity.*;

import java.sql.SQLException;

import com.hcmut.iotplatformservice.database.ConnectionPool;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

import java.util.List;
import java.util.Arrays;
import java.util.LinkedList;

public class MotorModel {

    private ConnectionPool _dbPool;
    private Logger _logger;

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

    public Boolean addMotor(String id, String position, String description, Boolean state, Integer relay) {
        String query = "INSERT INTO motor (`id`,`position`,`description`,`state`,`relay`) VALUES (?,?,?,?,?)";

        int count = _dbPool.execute(query, Arrays.asList(id, position, description, state, relay));

        if (count == 0)
            return false;

        return true;
    }

    public Boolean deleteMotorById(String id) {
        String query = "DELETE FROM motor WHERE id = ?";

        int count = _dbPool.execute(query, Arrays.asList(id));

        if (count == 0)
            return false;

        return true;
    }

    private MotorModel() {
        BasicConfigurator.configure();
        _dbPool = new ConnectionPool();
        _logger = Logger.getLogger(this.getClass());
    }

    private static class LazyHolder {
        public static final MotorModel _INSTANCE = new MotorModel();
    }

    public static MotorModel getInstance() {
        return LazyHolder._INSTANCE;
    }

}