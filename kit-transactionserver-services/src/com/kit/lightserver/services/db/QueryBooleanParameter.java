package com.kit.lightserver.services.db;

import com.fap.framework.db.QueryParameter;


public final class QueryBooleanParameter extends QueryParameter {

    private final Boolean value;

    public QueryBooleanParameter(final Boolean value) {
        this.value = value;
    }

    public Boolean getParameterValue() {
        return value;
    }

    @Override
    public Object getValue() {
        return value;
    }

}// class
