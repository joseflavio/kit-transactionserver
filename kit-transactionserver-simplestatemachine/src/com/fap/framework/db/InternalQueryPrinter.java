package com.fap.framework.db;

import java.util.List;


final class InternalQueryPrinter {

    private InternalQueryPrinter() {
    }

    static String printUpdateQuery(final UpdateQueryInterface updateQuery) {

        final String queryString = updateQuery.getPreparedUpdateQueryString();
        final List<QueryParameter> queryParameters = updateQuery.getUpdateQueryParameters();

        String replacedQueryString = queryString;
        for (final QueryParameter queryParam : queryParameters) {
            replacedQueryString = replacedQueryString.replaceFirst("\\?", queryParam.getValueToPrint().toString());
        }

        return replacedQueryString;

    }

    static String printSelectQuery(final SelectQueryInterface selectQuery) {

        final String queryString = selectQuery.getPreparedSelectQueryString();
        final List<QueryParameter> queryParameters = selectQuery.getSelectQueryParameters();

        String replacedQueryString = queryString;
        for (final QueryParameter queryParam : queryParameters) {
            replacedQueryString = replacedQueryString.replaceFirst("\\?", queryParam.getValueToPrint().toString());
        }

        return replacedQueryString;

    }

    static String printInsertQuery(final InsertQueryInterface insertQuery) {

        final String queryString = insertQuery.getPreparedInsertQueryString();
        final List<QueryParameter> queryParameters = insertQuery.getInsertQueryParameters();

        String replacedQueryString = queryString;
        for (final QueryParameter queryParam : queryParameters) {

            final Object paramValue = queryParam.getValueToPrint();

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

}// class
