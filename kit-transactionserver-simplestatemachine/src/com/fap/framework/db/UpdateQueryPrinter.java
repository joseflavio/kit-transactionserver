package com.fap.framework.db;


public final class UpdateQueryPrinter {

    private final UpdateQueryInterface query;

    public UpdateQueryPrinter(final UpdateQueryInterface query) {
        this.query = query;
    }

    @Override
    public String toString() {
        final String queryName = query.getClass().getCanonicalName();
        final String printedSelectQuery = InternalQueryPrinter.printUpdateQuery(query);
        final String toString = "[queryName="+ queryName + ", query=" + printedSelectQuery + "]";
        return toString;
    }

}// class
