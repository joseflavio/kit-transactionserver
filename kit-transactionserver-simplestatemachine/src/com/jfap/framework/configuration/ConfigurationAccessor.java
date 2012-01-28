package com.jfap.framework.configuration;

import java.util.Properties;

public final class ConfigurationAccessor {

    private final Properties properties;

    ConfigurationAccessor(final Properties properties) {
        this.properties = properties;
    }

    public boolean doesPropertyExists(final String propertyName) {
        final String propertyValue = properties.getProperty(propertyName);
        if (propertyValue == null) {
            return false;
        }
        return true;
    }

    public String getMandatoryProperty(final String propertyName) {
        final String propertyValue = properties.getProperty(propertyName);
        if (propertyValue == null) {
            throw new RuntimeException("Missing mandatory property. propertyName=" + propertyName);
        }
        else {
            return propertyValue;
        }
    }

    public <T> T getMandatoryProperty(final String propertyName, final TypeAdapter<T> adapter) {
        final String propertyValue = properties.getProperty(propertyName);
        if (propertyValue == null) {
            throw new RuntimeException("Missing mandatory property. propertyName=" + propertyName);
        }
        else {
            TypeAdapterResult<T> adapterResult = adapter.adapt(propertyValue);
            if( adapterResult.isSuccess() == false ) {
                throw new RuntimeException("Invalid value for mandatory property. propertyName=" + propertyName + ", propertyValue="+propertyValue);
            }
            return adapterResult.getValue();
        }
    }

}// class
