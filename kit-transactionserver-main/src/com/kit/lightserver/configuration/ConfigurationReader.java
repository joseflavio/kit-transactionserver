package com.kit.lightserver.configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ConfigurationReader {

    static private final Logger LOGGER = LoggerFactory.getLogger(ConfigurationReader.class);


    static public ConfigurationReaderResult loadConfiguration() {

        final Properties databaseProperties = ConfigurationReader.loadDatabaseProperties();
        if (databaseProperties == null) {
            return new ConfigurationReaderResult();
        }

        try {
            final String databaseConfiguration = ConfigurationReader.getMandatoryProperty(databaseProperties, "database.configuration");

            LOGGER.info("databaseConfiguration=" + databaseConfiguration);

        }
        catch (InvalidPropertiesFormatException e) {
            LOGGER.error("Configuration not valid.", e);
            return new ConfigurationReaderResult();
        }

        return new ConfigurationReaderResult();

    }

    static private String getMandatoryProperty(final Properties properties, final String propertyName) throws InvalidPropertiesFormatException {
        final String propertyValue = properties.getProperty(propertyName);
        if (propertyValue == null) {
            throw new InvalidPropertiesFormatException("Missing mandatory property. propertyName=" + propertyName);
        }
        else {
            return propertyValue;
        }
    }

    static private Properties loadDatabaseProperties() {
        try {
            FileInputStream in = new FileInputStream("database.properties");
            Properties properties = new Properties();
            properties.load(in);
            return properties;
        }
        catch (FileNotFoundException e) {
            LOGGER.error("Could no load properties.", e);
            return null;
        }
        catch (IOException e) {
            LOGGER.error("Could no load properties.", e);
            return null;
        }
    }

}// class
