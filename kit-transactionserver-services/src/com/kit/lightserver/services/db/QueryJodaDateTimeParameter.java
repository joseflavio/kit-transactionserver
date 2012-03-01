package com.kit.lightserver.services.db;

import org.joda.time.DateTime;

import com.fap.framework.db.QueryParameter;

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
