package com.kit.lightserver.services.db;

import com.fap.framework.db.QueryParameter;

public final class QueryStringParameter extends QueryParameter {

    private final String stringValue;

    public QueryStringParameter(final String stringValue) {
        this.stringValue = stringValue;
    }

    public String getParameterValue() {
        return stringValue;
    }

    @Override
    public Object getValue() {
        return stringValue;
    }

}// class
