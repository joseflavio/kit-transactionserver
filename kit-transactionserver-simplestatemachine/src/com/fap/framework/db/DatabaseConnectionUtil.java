package com.fap.framework.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fap.loggers.db.DatabaseLogger;

final class DatabaseConnectionUtil {

    static private final Logger LOGGER = LoggerFactory.getLogger(DatabaseConnectionUtil.class);

    static private final String driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    static private final DatabaseConnectionUtil INSTANCE = new DatabaseConnectionUtil();

    static public DatabaseConnectionUtil getInstance() {
        return INSTANCE;
    }

    private final AtomicInteger openConnectionsCount = new AtomicInteger(0);

    private DatabaseConnectionUtil() {

        /*
         * Loads Microsoft SQLServer Database Driver
         */
        try {
            Class.forName(driverClassName);
            DatabaseConnectionUtil.LOGGER.trace("Success loading jdbc driver class. driverClassName=" + driverClassName);
        }
        catch (final ClassNotFoundException e) {
            throw new RuntimeException("Could not load jdbc driver class. driverClassName=" + driverClassName, e);
        }

    }// constructor

    synchronized Connection getConnection(final DatabaseConfig dbConfig) {

        try {
            final Connection connection = DriverManager.getConnection(dbConfig.getDbUrl(), dbConfig.getDbUser(), dbConfig.getDbPassword());
            connection.setAutoCommit(true);
            openConnectionsCount.incrementAndGet();
            DatabaseLogger.logConnectionOpen(openConnectionsCount.intValue());
            LOGGER.warn("Getting a new db connection. openConnectionsCount=" + openConnectionsCount);
            return connection;
        }
        catch (final SQLException e) {
            LOGGER.error("Could not open a new connection. openConnectionsCount=" + openConnectionsCount, e);
            return null;
        }
        catch (Throwable t) {
            LOGGER.error("Unexpected. Could not open a new connection. openConnectionsCount=" + openConnectionsCount, t);
            return null;
        }

    }

    synchronized void closeConnection(final Connection connection) {

        try {

            if (connection == null) {
                DatabaseConnectionUtil.LOGGER.error("Null connection. Could not close it. openConnectionsCount=" + openConnectionsCount);
                return;
            }

            connection.close();
            openConnectionsCount.decrementAndGet();
            DatabaseLogger.logConnectionClosed(openConnectionsCount.intValue());
            LOGGER.warn("Closing a db connection. openConnectionsCount=" + openConnectionsCount);

        }
        catch (final SQLException e) {
            DatabaseConnectionUtil.LOGGER.error("Could not close the jdbc connection. openConnectionsCount=" + openConnectionsCount, e);
        }
        catch (Throwable t) {
            LOGGER.error("Unexpected. Could not open a new connection. openConnectionsCount=" + openConnectionsCount, t);
        }

    }

}// class
