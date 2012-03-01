package com.fap.framework.db;


public final class SelectQueryPrinter {

    private final SelectQueryInterface selectQuery;

    public SelectQueryPrinter(final SelectQueryInterface selectQuery) {
        this.selectQuery = selectQuery;
    }

    @Override
    public String toString() {
        final String queryName = selectQuery.getClass().getCanonicalName();
        final String printedSelectQuery = InternalQueryPrinter.printSelectQuery(selectQuery);
        final String toString = "[queryName="+ queryName + ", query=" + printedSelectQuery + "]";
        return toString;
    }

}// class
