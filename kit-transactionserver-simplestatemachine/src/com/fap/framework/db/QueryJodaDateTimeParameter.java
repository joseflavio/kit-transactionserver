package com.fap.framework.db;

import org.joda.time.DateTime;

public final class QueryJodaDateTimeParameter extends QueryParameter {

    private final DateTime value;

    public QueryJodaDateTimeParameter(final DateTime value) {
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
