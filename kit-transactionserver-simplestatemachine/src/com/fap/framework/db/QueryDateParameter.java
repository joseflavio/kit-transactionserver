package com.fap.framework.db;

import java.util.Date;

public final class QueryDateParameter extends QueryParameter {

    private final Date value;

    public QueryDateParameter(final Date value) {
        this.value = new Date(value.getTime());
    }

    public Date getParameterValue() {
        return new Date(value.getTime());
    }

    @Override
    public Object getValueToPrint() {
        return value.toString();
    }

}// class
