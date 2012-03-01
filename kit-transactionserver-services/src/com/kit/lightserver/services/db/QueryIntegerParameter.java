package com.kit.lightserver.services.db;

import com.fap.framework.db.QueryParameter;

public final class QueryIntegerParameter extends QueryParameter {

    private final Integer intValue;

    public QueryIntegerParameter(final int intValue) {
        this.intValue = intValue;
    }

    public int getParameterValue() {
        return intValue;
    }

    @Override
    public Object getValue() {
        return intValue;
    }

}// class
