package com.fap.framework.db;

public class DataSource {

    private final DbConnectionPool connectionPool;

    public DataSource(final DatabaseConfig dbConfig, final int connectionPoolSize) {
        this.connectionPool = new DbConnectionPool(dbConfig, connectionPoolSize);
    }

    public DbConnectionPool getConnectionPool() {
        return connectionPool;
    }

}
