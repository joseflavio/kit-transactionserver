package com.fap.framework.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fap.loggers.db.DatabaseLogger;

public final class DatabaseConnectionUtil {

    static private final Logger LOGGER = LoggerFactory.getLogger(DatabaseConnectionUtil.class);

    static private final String driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    static private final DatabaseConnectionUtil INSTANCE = new DatabaseConnectionUtil();

    static public DatabaseConnectionUtil getInstance2() {
        return INSTANCE;
    }

    private int openConnectionsCount = 0;

    private DatabaseConnectionUtil() {

        /*
         * Loads Microsoft SQLServer Database Driver
         */
        try {
            Class.forName(driverClassName);
            DatabaseConnectionUtil.LOGGER.info("Success loading jdbc driver class. driverClassName="+driverClassName);
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException("Could not load jdbc driver class. driverClassName="+driverClassName, e);
        }



    }// constructor

    public synchronized Connection getConnection(final DatabaseConfig dbConfig) {

        try {
            final Connection connection = DriverManager.getConnection(dbConfig.getDbUrl(), dbConfig.getDbUser(), dbConfig.getDbPassword());
            connection.setAutoCommit(true);
            ++openConnectionsCount;
            DatabaseLogger.logConnectionOpen(openConnectionsCount);
            return connection;
        } catch (final SQLException e) {
            LOGGER.error("Could not open a new connection. openConnectionsCount=" + openConnectionsCount, e);
            return null;
        }

    }

    public synchronized void closeConnection(final Connection connection) {
        try {
            connection.close();
            --openConnectionsCount;
            DatabaseLogger.logConnectionClosed(openConnectionsCount);
        }
        catch (final SQLException e) {
            DatabaseConnectionUtil.LOGGER.error("Could not close the jdbc connection. openConnectionsCount="+openConnectionsCount, e);
        }
    }

}// class
