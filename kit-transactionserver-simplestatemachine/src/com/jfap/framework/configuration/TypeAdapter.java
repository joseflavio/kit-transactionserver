package com.jfap.framework.configuration;

public interface TypeAdapter<T> {

    TypeAdapterResult<T> adapt(String propertyValue);

}
