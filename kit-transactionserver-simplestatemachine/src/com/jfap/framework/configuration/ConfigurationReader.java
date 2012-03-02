package com.jfap.framework.configuration;

import java.util.Properties;

public final class ConfigurationReader {

    static public ConfigAccessor getConfiguration(final String... externalPropertiesFilenames) {
        final Properties properties = new Properties();
        for(int i=0; i < externalPropertiesFilenames.length; ++i) {
            String currentPropertyFilename = externalPropertiesFilenames[i];
            PropertiesLoader.loadExternalProperties(properties, currentPropertyFilename);
        }
        ConfigurationAccessorImpl config = new ConfigurationAccessorImpl(properties);
        return config;
    }

}// class
