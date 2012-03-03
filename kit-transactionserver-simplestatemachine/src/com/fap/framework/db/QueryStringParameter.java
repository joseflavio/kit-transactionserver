package com.fap.framework.db;


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
