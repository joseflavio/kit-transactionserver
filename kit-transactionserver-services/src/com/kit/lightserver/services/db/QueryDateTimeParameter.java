package com.kit.lightserver.services.db;

import org.joda.time.DateTime;

public final class QueryDateTimeParameter extends QueryParameter {

    private final DateTime value;

    public QueryDateTimeParameter(final DateTime value) {
        this.value = value;
    }

    public DateTime getParameterValue() {
        return value;
    }

    @Override
    public Object getValue() {
        return value;
    }

}// class
