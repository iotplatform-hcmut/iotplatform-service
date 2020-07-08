package com.hcmut.iotplatformservice.model;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.hcmut.iotplatformservice.database.ConnectionPool;
import com.hcmut.iotplatformservice.entity.MoisEntity;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

public class MoisModel {

    private ConnectionPool _dbPool;
    private Logger _logger;

    public List<MoisEntity> getValueByDeviceId(List<String> ids, int limit, int start, int end) {
        String query = "CALL sp_get_mois_stats(?,?,?,?)";

        List<Object> params = Arrays.asList(String.join(",", ids), start, end, limit);

        Map<String, List<Integer>> datamap = new HashMap<String, List<Integer>>();
        ids.forEach(id -> datamap.put(id, new LinkedList<Integer>()));

        _dbPool.execute(query, params, rs -> {
            try {
                String id = rs.getString("id");
                Integer value = rs.getInt("value");
                datamap.get(id).add(value);
            } catch (SQLException ex) {
                _logger.info(ex.getMessage(), ex);
            }
        });

        List<MoisEntity> lsEntity = new LinkedList<>();

        datamap.forEach((id, values) -> {
            MoisEntity entity = new MoisEntity();
            entity.setId(id);
            entity.setValues(values);
            lsEntity.add(entity);
        });

        return lsEntity;
    }


    private MoisModel() {
        BasicConfigurator.configure();
        _dbPool = new ConnectionPool();
        _logger = Logger.getLogger(this.getClass());
    }

    private static class LazyHolder {
        public static final MoisModel _INSTANCE = new MoisModel();
    }

    public static MoisModel getInstance() {
        return LazyHolder._INSTANCE;
    }

}