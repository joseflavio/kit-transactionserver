package com.jfap.framework.configuration;

import java.util.Properties;

public final class ConfigurationReader {

    static private final String databasePropertiesFilename = "/config/database.properties";

    private final Properties properties = new Properties();

    private ConfigurationReader() {
        PropertiesLoader.loadInternalClasspathProperties(properties, databasePropertiesFilename);
    }

    static private ConfigurationReader INSTANCE = new ConfigurationReader();

    static public ConfigAccessor getConfiguration() {
        ConfigurationAccessorImpl config = new ConfigurationAccessorImpl(INSTANCE.properties);
        return config;
    }

}// class
