package com.fap.framework.db;

public final class SelectQueryResultSingleBoolean {

    private final boolean available;

    private final boolean value;

    public SelectQueryResultSingleBoolean(final boolean value) {
        this.available = true;
        this.value = value;
    }

    public SelectQueryResultSingleBoolean() {
        this.available = false;
        this.value = false;
    }

    public boolean isAvailable() {
        return available;
    }

    public boolean getValue() {
        if( available == false ) {
            throw new RuntimeException("Value not available");
        }
        return value;
    }



}// class
