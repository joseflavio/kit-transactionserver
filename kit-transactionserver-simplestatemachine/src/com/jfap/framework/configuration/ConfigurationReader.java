package com.jfap.framework.configuration;

import java.util.Properties;

public final class ConfigurationReader {

    static public final String databasePropertiesFilenameInternal = "/config/internal.properties"; // root

    static private final String SERVER_PROPERTIES="config/server.properties";
    static private final String DATABASE_PROPERTIES = "config/database.properties"; // relative

    private final Properties properties = new Properties();

    private ConfigurationReader() {
        PropertiesLoader.loadExternalProperties(properties, SERVER_PROPERTIES);
        PropertiesLoader.loadExternalProperties(properties, DATABASE_PROPERTIES);
    }

    static private ConfigurationReader INSTANCE = new ConfigurationReader();

    static public ConfigAccessor getConfiguration() {
        ConfigurationAccessorImpl config = new ConfigurationAccessorImpl(INSTANCE.properties);
        return config;
    }

}// class
