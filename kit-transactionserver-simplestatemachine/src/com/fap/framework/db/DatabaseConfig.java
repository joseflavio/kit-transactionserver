package com.fap.framework.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fap.framework.adapters.IntegerAdapter;
import com.jfap.framework.configuration.ConfigAccessor;

final public class DatabaseConfig {

    static private final Logger LOGGER = LoggerFactory.getLogger(DatabaseConfig.class);

    static public DatabaseConfig getInstance(final ConfigAccessor accessor) {
        final DatabaseConfig dbConfig = new DatabaseConfig(accessor);
        LOGGER.info("Configuration loaded. dbConfig="+dbConfig);
        return dbConfig;
    }

    //static private final ConfigurationAccessor CONFIG = ConfigurationReader.getConfiguration(DatabaseConfiguration.class);

    private final String dbHost;
    private final int dbPort;
    private final String dbName;
    private final String dbUser;
    private final String dbPassword;

    private DatabaseConfig(final ConfigAccessor accessor) {
        this.dbHost = accessor.getMandatoryProperty("database.host");
        this.dbPort = accessor.getMandatoryProperty("database.port", new IntegerAdapter()).intValue();
        this.dbName = accessor.getMandatoryProperty("database.name");
        this.dbUser = accessor.getMandatoryProperty("database.user");
        this.dbPassword = accessor.getMandatoryProperty("database.password");
    }

    public String getDbUrl() {
        final String dbJdbcConnectionUrl = "jdbc:sqlserver://" + dbHost + ":" + dbPort + ";databaseName=" + dbName;
        return dbJdbcConnectionUrl;
    }

    public String getDbName() {
        final String dbJdbcConnectionUrl = dbHost + ":" + dbPort + "/" + dbName;
        return dbJdbcConnectionUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    @Override
    public String toString() {
        return "DatabaseConfiguration [dbHost=" + dbHost + ", dbPort=" + dbPort + ", dbName=" + dbName + ", dbUser=" + dbUser + ", dbPassword=" + dbPassword
                + "]";
    }

}// class
