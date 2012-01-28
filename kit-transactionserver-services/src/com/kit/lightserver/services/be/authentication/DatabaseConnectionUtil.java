package com.kit.lightserver.services.be.authentication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DatabaseConnectionUtil {

    static private final Logger LOGGER = LoggerFactory.getLogger(DatabaseConnectionUtil.class);

    static private final DatabaseConnectionUtil INSTANCE = new DatabaseConnectionUtil();

    static private final String driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    static public DatabaseConnectionUtil getInstance() {
        return INSTANCE;
    }

    private final DatabaseConfiguration dbConfig = new DatabaseConfiguration();

    private int openConnectionsCount = 0;

    private DatabaseConnectionUtil() {

        LOGGER.info("Database configuration loaded. dbUrl="+dbConfig.getDbUrl());

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

    public synchronized Connection getConnection() {

        try {
            final Connection connection = DriverManager.getConnection(dbConfig.getDbUrl(), dbConfig.getDbUser(), dbConfig.getDbPassword());
            connection.setAutoCommit(true);
            ++openConnectionsCount;
            LOGGER.info("Connection open. openConnectionsCount="+openConnectionsCount);
            return connection;
        } catch (final SQLException e) {
            LOGGER.warn("Could not open a new connection. openConnectionsCount=" + openConnectionsCount, e);
            return null;
        }


    }

    public synchronized void closeConnection(final Connection connection) {
        try {
            connection.close();
            --openConnectionsCount;
            DatabaseConnectionUtil.LOGGER.info("Connection closed. openConnectionsCount="+openConnectionsCount);
        }
        catch (final SQLException e) {
            DatabaseConnectionUtil.LOGGER.error("Could not close the jdbc connection. openConnectionsCount="+openConnectionsCount, e);
        }
    }

}// class
