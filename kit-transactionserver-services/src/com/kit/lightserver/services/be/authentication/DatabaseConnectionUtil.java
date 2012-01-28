package com.kit.lightserver.services.be.authentication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfap.framework.configuration.ConfigurationAccessor;
import com.jfap.framework.configuration.ConfigurationReader;
import com.jfap.framework.configuration.IntegerAdapter;

public final class DatabaseConnectionUtil {

    static private final Logger LOGGER = LoggerFactory.getLogger(DatabaseConnectionUtil.class);

    static private final ConfigurationAccessor CONFIG = ConfigurationReader.getConfiguration(DatabaseConnectionUtil.class);

    static private boolean driverLoaded = false;

    static private int openConnectionsCount = 0;

    static public Connection getConnection() {

        if( DatabaseConnectionUtil.driverLoaded == false ) {
            DatabaseConnectionUtil.loadMicrosoftSQLServerDatabaseDriver();
        }

        final String dbHost = CONFIG.getMandatoryProperty("database.host");
        final Integer dbPort = CONFIG.getMandatoryProperty("database.port", new IntegerAdapter());
        final String dbName = CONFIG.getMandatoryProperty("database.name");
        final String dbUser = CONFIG.getMandatoryProperty("database.user");
        final String dbPassword = CONFIG.getMandatoryProperty("database.password");

        final String dbUrl = "jdbc:sqlserver://" + dbHost + ":" + dbPort + ";databaseName=" + dbName;


        try {
            LOGGER.info("Getting db connection. dbUrl="+dbUrl);
            final Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            connection.setAutoCommit(true);
            ++DatabaseConnectionUtil.openConnectionsCount;
            LOGGER.info("Connection open. openConnectionsCount="+DatabaseConnectionUtil.openConnectionsCount);
            return connection;


        } catch (final SQLException e) {
            LOGGER.warn("Could not open a new connection. openConnectionsCount=" + DatabaseConnectionUtil.openConnectionsCount, e);
            return null;
        }


    }

    static private void loadMicrosoftSQLServerDatabaseDriver() {
        final String driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        try {
            Class.forName(driverClassName);
            DatabaseConnectionUtil.driverLoaded = true;
            DatabaseConnectionUtil.LOGGER.info("Success loading jdbc driver class. driverClassName="+driverClassName);
        } catch (final ClassNotFoundException e) {
            DatabaseConnectionUtil.LOGGER.error("Could not find jdbc driver class. driverClassName="+driverClassName, e);
        }
    }

    static public void closeConnection(final Connection connection) {
        try {
            connection.close();
            --DatabaseConnectionUtil.openConnectionsCount;
            DatabaseConnectionUtil.LOGGER.info("Connection closed. openConnectionsCount="+DatabaseConnectionUtil.openConnectionsCount);
        }
        catch (final SQLException e) {
            DatabaseConnectionUtil.LOGGER.error("Could not close the jdbc connection. openConnectionsCount="+DatabaseConnectionUtil.openConnectionsCount, e);
        }
    }

}// class
