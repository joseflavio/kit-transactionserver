package com.jfap.framework.configuration;

public interface ConfigAccessor {

    String getMandatoryProperty(String key);

    <T> T getMandatoryProperty(final String propertyName, final TypeAdapter<T> adapter);

}
