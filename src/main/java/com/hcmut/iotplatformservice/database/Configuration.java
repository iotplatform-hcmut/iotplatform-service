package com.hcmut.iotplatformservice.database;

import lombok.Getter;

@Getter
class Configuration {
    private String username = "root";
    private String password = "a123";
    private String schema = "iotplatform";
    private String host = "35.202.152.182";
    private String port = "3306";
    private int minConnection = 2;
    private int maxConnection = 10;
    private String connectionString = "jdbc:mysql://%s:%s/%s";
    public Configuration() {
        connectionString = String.format(connectionString, host, port, schema);
    }
}