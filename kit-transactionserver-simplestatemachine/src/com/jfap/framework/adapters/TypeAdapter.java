package com.jfap.framework.adapters;

public interface TypeAdapter<T, K> {

    TypeAdapterResult<T> adapt(K value);

}
