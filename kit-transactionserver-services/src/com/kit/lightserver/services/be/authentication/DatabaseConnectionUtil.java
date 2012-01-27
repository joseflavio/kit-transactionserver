package com.kit.lightserver.services.be.authentication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfap.framework.configuration.ConfigurationReader;
import com.jfap.framework.configuration.ConfigurationReaderResult;

public final class DatabaseConnectionUtil {

    static private final Logger LOGGER = LoggerFactory.getLogger(DatabaseConnectionUtil.class);

    static private boolean driverLoaded = false;

    static private int openConnectionsCount = 0;

    static public Connection getConnection() {

        if( DatabaseConnectionUtil.driverLoaded == false ) {
            DatabaseConnectionUtil.loadMicrosoftSQLServerDatabaseDriver();
        }

        // jdbc:sqlserver://srvkit.tinet.com.br:1433;databaseName=TESTDEV_JOSEFLAVIO_KEEPIN3_MIRA_DBV20111129;
        // jdbc:sqlserver://192.168.1.41:1433;databaseName=KEEPIN3_MIRA_DBV20110708; // 192.168.1.41 = Fenris

        ConfigurationReaderResult configuration = ConfigurationReader.loadConfiguration();

        final String dbHost = "srvkit.tinet.com.br";
        final int dbPort = 1433;
        final String dbName = "TESTDEV_JOSEFLAVIO_KEEPIN3_MIRA_DBV20111129";
        final String dbUser = "sa";
        final String dbPassword = "chicabom";

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
