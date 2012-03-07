package com.fap.framework.db;



public final class QueryBooleanParameter extends QueryParameter {

    private final Boolean value;

    public QueryBooleanParameter(final Boolean value) {
        this.value = value;
    }

    public Boolean getParameterValue() {
        return value;
    }

    @Override
    public Object getValueToPrint() {
        return value;
    }

}// class
