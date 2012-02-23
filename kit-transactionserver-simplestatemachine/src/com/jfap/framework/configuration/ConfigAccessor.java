package com.jfap.framework.configuration;

public interface ConfigAccessor {

    String getMandatoryProperty(String propertyName);

    <T> T getMandatoryProperty(final String propertyName, final TypeAdapter<T, String> adapter);

    String getOptionalProperty(String propertyName, String defaultValue);

}// interface
