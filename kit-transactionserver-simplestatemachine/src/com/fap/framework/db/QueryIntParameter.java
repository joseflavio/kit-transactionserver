package com.fap.framework.db;


public final class QueryIntParameter extends QueryParameter {

    private final int intValue;

    public QueryIntParameter(final int intValue) {
        this.intValue = intValue;
    }

    public int getParameterValue() {
        return intValue;
    }

    @Override
    public Object getValueToPrint() {
        return Integer.valueOf(intValue);
    }

}// class
