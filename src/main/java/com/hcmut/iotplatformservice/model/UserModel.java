package com.hcmut.iotplatformservice.model;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.hcmut.iotplatformservice.database.ConnectionPool;
import com.hcmut.iotplatformservice.entity.UserEntity;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

public class UserModel {

    private ConnectionPool _dbPool = new ConnectionPool();
    private static final Logger _logger = Logger.getLogger(UserModel.class);

    public Boolean addUser(String name, String username, String password, String email, long phone, int birthday) {
        String query = "INSERT INTO user (name, username, password, email, phone, birthday) VALUES (?,?,?,?,?,?)";
        List<Object> arrParam = Arrays.asList(name, username, password, email, phone, birthday);

        int count = _dbPool.execute(query, arrParam);

        if (count == 0)
            return false;
        
        return true;
    }

    public List<UserEntity> getAllUser() {
        List<UserEntity> lsUser = new LinkedList<>();

        String query = "SELECT * from user";

        _dbPool.execute(query, rs -> {
            try {
                UserEntity entity = new UserEntity();
                entity.setId(rs.getInt("id"));
                entity.setName(rs.getString("name"));
                entity.setUsername(rs.getString("username"));
                entity.setEmail(rs.getString("email"));
                entity.setPhone(rs.getLong("phone"));
                entity.setBirthday(rs.getInt("birthday"));
                lsUser.add(entity);
            } catch (SQLException ex) {
                _logger.info(ex.getMessage(), ex);
            }
        });

        return lsUser;
    }

    public Boolean deleteUser(Integer id) {
        String query = "DELETE FROM user WHERE id = ?";

        List<Object> params = new LinkedList<>();
        params.add(id);

        int count = _dbPool.execute(query, params);

        if (count == 0)
            return false;

        return true;
    }

    private UserModel() {
        BasicConfigurator.configure();
    }

    private static class LazyHolder {
        public static final UserModel _INSTANCE = new UserModel();
    }

    public static UserModel getInstance() {
        return LazyHolder._INSTANCE;
    }
}