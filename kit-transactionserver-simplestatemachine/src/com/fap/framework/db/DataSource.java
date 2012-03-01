package com.fap.framework.db;

public class DataSource {

    public DataSource(final DatabaseConfig dbConfig) {
        DbConnectionPool connectionPool = new DbConnectionPool(dbConfig);
    }

}
