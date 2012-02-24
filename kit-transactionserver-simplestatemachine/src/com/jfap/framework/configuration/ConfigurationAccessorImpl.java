package com.jfap.framework.configuration;

import java.util.Properties;

import com.jfap.framework.adapters.TypeAdapter;
import com.jfap.framework.adapters.TypeAdapterResult;

public final class ConfigurationAccessorImpl implements ConfigAccessor {

    private final Properties properties;

    public ConfigurationAccessorImpl(final Properties properties) {
        this.properties = properties;
    }

    public boolean doesPropertyExists(final String propertyName) {
        final String propertyValue = properties.getProperty(propertyName);
        if (propertyValue == null) {
            return false;
        }
        return true;
    }

    @Override
    public String getMandatoryProperty(final String propertyName) {
        final String propertyValue = properties.getProperty(propertyName);
        if (propertyValue == null) {
            throw new RuntimeException("Missing mandatory property. propertyName=" + propertyName);
        }
        else {
            return propertyValue;
        }
    }

    @Override
    public String getOptionalProperty(final String propertyName, final String defaultValue) {
        final String propertyValue = properties.getProperty(propertyName);
        if (propertyValue == null) {
            return defaultValue;
        }
        else {
            return propertyValue;
        }
    }

    @Override
    public <T> T getMandatoryProperty(final String propertyName, final TypeAdapter<T, String> adapter) {
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

    @Override
    public <T> T getOptionalProperty(final String propertyName, final TypeAdapter<T, String> adapter, final T defaultValue) {
        final String propertyValue = properties.getProperty(propertyName);
        TypeAdapterResult<T> adapterResult = adapter.adapt(propertyValue);
        if( adapterResult.isSuccess() == false ) {
            return defaultValue;
        }
        return adapterResult.getValue();
    }



}// class
