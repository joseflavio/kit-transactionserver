package com.kit.lightserver.services.db;

import java.util.List;

public final class QueryPrinter {

    private QueryPrinter() {
    }

    static public String printUpdateQuery(final UpdateQueryInterface updateQuery) {

        final String queryString = updateQuery.getPreparedUpdateQueryString();
        final List<QueryParameter> queryParameters = updateQuery.getUpdateQueryParameters();

        String replacedQueryString = queryString;
        for (final QueryParameter queryParam : queryParameters) {
            replacedQueryString = replacedQueryString.replaceFirst("\\?", queryParam.getValue().toString());
        }

        return replacedQueryString;

    }

    static public String printSelectQuery(final SelectQueryInterface selectQuery) {

        final String queryString = selectQuery.getPreparedSelectQueryString();
        final List<QueryParameter> queryParameters = selectQuery.getSelectQueryParameters();

        String replacedQueryString = queryString;
        for (final QueryParameter queryParam : queryParameters) {
            replacedQueryString = replacedQueryString.replaceFirst("\\?", queryParam.getValue().toString());
        }

        return replacedQueryString;

    }

    static public String printInsertQuery(final InsertQueryInterface insertQuery) {

        final String queryString = insertQuery.getPreparedInsertQueryString();
        final List<QueryParameter> queryParameters = insertQuery.getInsertQueryParameters();

        String replacedQueryString = queryString;
        for (final QueryParameter queryParam : queryParameters) {

            final Object paramValue = queryParam.getValue();

            final String printedParam;
            if( paramValue instanceof String ) {
                printedParam = "'" + (String)paramValue + "'";
            }
            else {
                printedParam = paramValue.toString();
            }

            replacedQueryString = replacedQueryString.replaceFirst("\\?", printedParam);
        }

        return replacedQueryString;

    }

    static public String toString(final InsertQueryInterface insertQuery) {
        final String printedQuery = QueryPrinter.printInsertQuery(insertQuery);
        final String queryName = insertQuery.getClass().getCanonicalName();
        return queryName + "[ " + printedQuery + " ]";
    }

}// class
