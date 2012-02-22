package com.kit.lightserver.services.be.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfap.framework.configuration.ConfigAccessor;
import com.jfap.framework.configuration.IntegerAdapter;

final public class DatabaseConfiguration {

    static private final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    static public DatabaseConfiguration getInstance(final ConfigAccessor accessor) {
        final DatabaseConfiguration dbConfig = new DatabaseConfiguration(accessor);
        LOGGER.info("Configuration loaded. dbConfig="+dbConfig);
        return dbConfig;
    }

    //static private final ConfigurationAccessor CONFIG = ConfigurationReader.getConfiguration(DatabaseConfiguration.class);

    private final String dbHost;
    private final int dbPort;
    private final String dbName;
    private final String dbUser;
    private final String dbPassword;

    private DatabaseConfiguration(final ConfigAccessor accessor) {
        this.dbHost = accessor.getMandatoryProperty("database.host");
        this.dbPort = accessor.getMandatoryProperty("database.port", new IntegerAdapter()).intValue();
        this.dbName = accessor.getMandatoryProperty("database.name");
        this.dbUser = accessor.getMandatoryProperty("database.user");
        this.dbPassword = accessor.getMandatoryProperty("database.password");
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

    @Override
    public String toString() {
        return "DatabaseConfiguration [dbHost=" + dbHost + ", dbPort=" + dbPort + ", dbName=" + dbName + ", dbUser=" + dbUser + ", dbPassword=" + dbPassword
                + "]";
    }

}// class
