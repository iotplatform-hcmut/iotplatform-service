package com.hcmut.iotplatformservice.database;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectionPool {

    private HikariConfig hikariConfig;
    private HikariDataSource hikariDataSource;
    private Configuration databaseConfig;

    private static ConnectionPool _INSTANCE;

    private ConnectionPool() {
        try {
            databaseConfig = new Configuration();
            hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(databaseConfig.getConnectionString());
            hikariConfig.setUsername(databaseConfig.getUsername());
            hikariConfig.setPassword(databaseConfig.getPassword());
            hikariConfig.setMinimumIdle(databaseConfig.getMinConnection());
            hikariConfig.setMaximumPoolSize(databaseConfig.getMaxConnection());
            hikariDataSource = new HikariDataSource(hikariConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Static block initialization for exception handling
    static {
        try {
            _INSTANCE = new ConnectionPool();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return _INSTANCE.hikariDataSource.getConnection();
    }
}