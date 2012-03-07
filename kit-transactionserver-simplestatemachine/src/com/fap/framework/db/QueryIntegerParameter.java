package com.fap.framework.db;


public final class QueryIntegerParameter extends QueryParameter {

    private final Integer intValue;

    public QueryIntegerParameter(final Integer intValue) {
        this.intValue = intValue;
    }

    public Integer getParameterValue() {
        return intValue;
    }

    @Override
    public Object getValueToPrint() {
        return intValue;
    }

}// class
