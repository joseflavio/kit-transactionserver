package com.jfap.framework.configuration;

public interface TypeAdapter<T, K> {

    TypeAdapterResult<T> adapt(K origin);

}
