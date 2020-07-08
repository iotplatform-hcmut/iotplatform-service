package com.hcmut.iotplatformservice.model;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.hcmut.iotplatformservice.database.ConnectionPool;
import com.hcmut.iotplatformservice.entity.SensorEntity;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class SensorModel {
    private ConnectionPool _dbPool;
    private Logger _logger;

    public List<SensorEntity> getAllSensor() {
        List<SensorEntity> lsEntity = new LinkedList<>();

        String query = "SELECT * FROM sensor";

        _dbPool.execute(query, rs -> {
            try {
                SensorEntity entity = new SensorEntity();
                entity.setId(rs.getString("id"));
                entity.setPosition(rs.getString("position"));
                entity.setDescription(rs.getString("description"));
                entity.setState(rs.getBoolean("state"));
                entity.setRelay(rs.getInt("relay"));
                entity.setHistory(rs.getString("history"));
                lsEntity.add(entity);
            } catch (SQLException ex) {
                _logger.info(ex.getMessage(), ex);
            }
        });

        return lsEntity;
    }

    public Boolean addSensor(String id, String position, String description, Boolean state, int relay, String history) {
        String query = "INSERT INTO sensor (`id`,`position`,`description`,`state`,`relay`,`history`) VALUES (?,?,?,?,?,?)";

        int count = _dbPool.execute(query, Arrays.asList(id, position, description, state, relay, history));

        if (count == 0)
            return false;

        return true;
    }

    public Boolean deleteSensorById(String id) {
        String query = "DELETE FROM sensor WHERE id = ?";

        int count = _dbPool.execute(query, Arrays.asList(id));

        if (count == 0)
            return false;

        return true;
    }

    public static void main(String[] args) {
        SensorModel.getInstance().addSensor("a", "a", "a", false, 0, "a");
        System.out.println(SensorModel.getInstance().getAllSensor().toString());
        SensorModel.getInstance().deleteSensorById("a");
        System.out.println(SensorModel.getInstance().getAllSensor().toString());
    }

    private SensorModel() {
        BasicConfigurator.configure();
        _dbPool = new ConnectionPool();
        _logger = Logger.getLogger(this.getClass());
    }

    private static class LazyHolder {
        static final SensorModel _INSTANCE = new SensorModel();
    }

    public static SensorModel getInstance() {
        return LazyHolder._INSTANCE;
    }
}