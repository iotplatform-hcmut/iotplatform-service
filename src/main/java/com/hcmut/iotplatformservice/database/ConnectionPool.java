package com.hcmut.iotplatformservice.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.function.Consumer;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class ConnectionPool {

    private static final Logger _logger = Logger.getLogger(ConnectionPool.class);
    private HikariDataSource _dataSource;

    public ConnectionPool() {
        BasicConfigurator.configure();
        try {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl("jdbc:mysql://iotplatform.xyz:3306/iotplatform");
            hikariConfig.setUsername("root");
            hikariConfig.setPassword("a123");
            hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
            hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
            hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
            hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            _dataSource = new HikariDataSource(hikariConfig);
        } catch (Exception ex) {
            _logger.info(ex.getMessage(), ex);
        }
    }

    private void close(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception ex) {
            _logger.info(ex.getMessage(), ex);
        }
    }

    private void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception ex) {
            _logger.info(ex.getMessage(), ex);
        }
    }

    private void close(Statement st) {
        try {
            if (st != null) {
                st.close();
            }
        } catch (Exception ex) {
            _logger.info(ex.getMessage(), ex);
        }
    }

    private void addParam(PreparedStatement stmt, Object[] arrParam) {
        try {
            for (int idxParam = 0; idxParam < arrParam.length; idxParam++) {
                Object param = arrParam[idxParam];
                int pos = idxParam + 1;
                stmt.setObject(pos, param);
            }
        } catch (Exception ex) {
            _logger.info(ex.getMessage(), ex);
        }
    }

    public void execute(String query, Consumer<ResultSet> func) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = _dataSource.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                if (func == null) {
                    continue;
                }
                func.accept(rs);
            }
        } catch (Exception ex) {
            _logger.info(ex.getMessage(), ex);
        } finally {
            close(stmt);
            close(rs);
            close(conn);
        }
    }

    public int execute(String query) {
        Connection conn = null;
        Statement stmt = null;
        int res = 0;

        try {
            conn = _dataSource.getConnection();
            stmt = conn.createStatement();
            res = stmt.executeUpdate(query);
        } catch (Exception ex) {
            _logger.info(ex.getMessage(), ex);
        } finally {
            close(stmt);
            close(conn);
        }

        return res;
    }

    public void execute(String query, Object[] arrParam, Consumer<ResultSet> func) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = _dataSource.getConnection();
            stmt = conn.prepareStatement(query);
            addParam(stmt, arrParam);
            rs = stmt.executeQuery();
            while (rs.next()) {
                if (func == null) {
                    continue;
                }
                func.accept(rs);
            }
        } catch (Exception ex) {
            _logger.info(ex.getMessage(), ex);
        } finally {
            close(stmt);
            close(rs);
            close(conn);
        }
    }

    public int execute(String query, Object[] arrParam) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int res = 0;

        try {
            conn = _dataSource.getConnection();
            stmt = conn.prepareStatement(query);
            addParam(stmt, arrParam);
            res = stmt.executeUpdate();
        } catch (Exception ex) {
            _logger.info(ex.getMessage(), ex);
        } finally {
            close(stmt);
            close(conn);
        }

        return res;
    }

    public void executeBatch(String query, List<Object[]> listArrParam) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            if (listArrParam.size() > 200) {
                throw new Exception("Batch too large");
            }
            conn = _dataSource.getConnection();
            stmt = conn.prepareStatement(query);
            for (Object[] arrParam : listArrParam) {
                addParam(stmt, arrParam);
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (Exception ex) {
            _logger.info(ex.getMessage(), ex);
        } finally {
            close(stmt);
            close(conn);
        }
    }

    public void executeBatch(List<String> listQuery) {
        Connection conn = null;
        Statement stmt = null;
        try {
            if (listQuery.size() > 200) {
                throw new Exception("Batch too large");
            }
            conn = _dataSource.getConnection();
            stmt = conn.createStatement();
            for (String query : listQuery) {
                stmt.addBatch(query);
            }
            stmt.executeBatch();
        } catch (Exception ex) {
            _logger.info(ex.getMessage(), ex);
        } finally {
            close(stmt);
            close(conn);
        }
    }

    static public String genQuestion(int m) {
        StringBuilder res = new StringBuilder();
        while (m > 1) {
            res.append("?,");
            --m;
        }
        res.append('?');
        return res.toString();
    }
}