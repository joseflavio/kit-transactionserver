package com.jfap.framework.configuration;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ConfigurationReader {

    static private final Logger LOGGER = LoggerFactory.getLogger(ConfigurationReader.class);

    static private final String databasePropertiesFilename = "/config/database.properties";

    private final Properties properties = new Properties();

    private ConfigurationReader() {
        PropertiesLoader.loadInternalClasspathProperties(properties, databasePropertiesFilename);
    }

    static private ConfigurationReader INSTANCE = new ConfigurationReader();

    static public ConfigurationAccessor getConfiguration(final Class<?> origin) {
        LOGGER.info("New configuration instance. source="+origin);
        ConfigurationAccessor config = new ConfigurationAccessor(INSTANCE.properties);
        return config;
    }

}// class
