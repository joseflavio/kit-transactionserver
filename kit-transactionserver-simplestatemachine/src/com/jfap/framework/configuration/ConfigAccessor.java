package com.jfap.framework.configuration;

import com.fap.framework.adapters.TypeAdapter;

public interface ConfigAccessor {

    String getMandatoryProperty(String propertyName);

    <T> T getMandatoryProperty(String propertyName, TypeAdapter<T, String> adapter);

    String getOptionalProperty(String propertyName, String defaultValue);

    <T> T getOptionalProperty(String propertyName, TypeAdapter<T, String> adapter, T defaultValue);

}// interface
