package com.hcmut.iotplatformservice.model;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.hcmut.iotplatformservice.database.ConnectionPool;
import com.hcmut.iotplatformservice.entity.AnomalyEntity;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class AnomalyModel {

    private Logger _logger;
    private ConnectionPool _dbPool;

    public AnomalyEntity getAnomaly() {
        AnomalyEntity entity = new AnomalyEntity();

        String query = "SELECT * FROM stl";

        List<Float> origin = new LinkedList<>();
        List<Float> season = new LinkedList<>();
        List<Float> trend = new LinkedList<>();
        List<Float> residual = new LinkedList<>();

        _dbPool.execute(query, rs -> {
            try {
                origin.add(rs.getFloat("origin"));
                season.add(rs.getFloat("season"));
                trend.add(rs.getFloat("trend"));
                residual.add(rs.getFloat("residual"));
               
            } catch (SQLException ex) {
                _logger.info(ex.getMessage(), ex);
            }
        });

        entity.setOrigin(origin);
        entity.setSeason(season);
        entity.setTrend(trend);
        entity.setResidual(residual);

        return entity;
    }

    private AnomalyModel() {
        BasicConfigurator.configure();
        _logger = Logger.getLogger(this.getClass());
        _dbPool = new ConnectionPool();
    }

    private static class LazyHolder {
        public static final AnomalyModel _INSTANCE = new AnomalyModel();
    }

    public static AnomalyModel getInstance() {
        return LazyHolder._INSTANCE;
    }
}