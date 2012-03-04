package com.fap.framework.adapters;

public final class TypeAdapterResult<T> {

    private final boolean success;
    private final T value;

    TypeAdapterResult() {
        this.success = false;
        this.value = null;
    }

    TypeAdapterResult(final T value) {
        this.success = true;
        this.value = value;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getValue() {
        return value;
    }

}// class