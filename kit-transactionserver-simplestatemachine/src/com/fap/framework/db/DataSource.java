package com.fap.framework.db;

public class DataSource {

    private final DbConnectionPool connectionPool;

    public DataSource(final DatabaseConfig dbConfig) {
        this.connectionPool = new DbConnectionPool(dbConfig);
    }

    public DbConnectionPool getConnectionPool() {
        return connectionPool;
    }

}
