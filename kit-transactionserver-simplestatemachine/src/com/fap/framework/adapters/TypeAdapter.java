package com.fap.framework.adapters;

public interface TypeAdapter<T, K> {

    TypeAdapterResult<T> adapt(K value);

}
