package com.kit.lightserver.services.be.authentication;

import com.jfap.framework.configuration.ConfigurationAccessor;
import com.jfap.framework.configuration.ConfigurationReader;
import com.jfap.framework.configuration.IntegerAdapter;

final class DatabaseConfiguration {

    static private final ConfigurationAccessor CONFIG = ConfigurationReader.getConfiguration(DatabaseConfiguration.class);

    private final String dbHost;
    private final int dbPort;
    private final String dbName;
    private final String dbUser;
    private final String dbPassword;

    DatabaseConfiguration() {
        this.dbHost = CONFIG.getMandatoryProperty("database.host");
        this.dbPort = CONFIG.getMandatoryProperty("database.port", new IntegerAdapter()).intValue();
        this.dbName = CONFIG.getMandatoryProperty("database.name");
        this.dbUser = CONFIG.getMandatoryProperty("database.user");
        this.dbPassword = CONFIG.getMandatoryProperty("database.password");
    }

    String getDbUrl() {
        final String dbJdbcConnectionUrl = "jdbc:sqlserver://" + dbHost + ":" + dbPort + ";databaseName=" + dbName;
        return dbJdbcConnectionUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }


}// class
