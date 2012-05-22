package com.fap.framework.db;


import java.util.Date;
import java.util.List;


final class InternalQueryPrinter {

    private InternalQueryPrinter() {
    }

    static String printUpdateQuery(final UpdateQueryInterface updateQuery) {

        final String queryString = updateQuery.getPreparedUpdateQueryString();
        final List<QueryParameter> queryParameters = updateQuery.getUpdateQueryParameters();

        String replacedQueryString = queryString;
        for (final QueryParameter queryParam : queryParameters) {
            final Object paramValue = queryParam.getValueToPrint();
            final String printedParam = InternalQueryPrinter.convertParameterToPrint(paramValue);
            replacedQueryString = replacedQueryString.replaceFirst("\\?", printedParam);
        }

        return replacedQueryString;

    }

    static String printSelectQuery(final SelectQueryInterface selectQuery) {

        final String queryString = selectQuery.getPreparedSelectQueryString();
        final List<QueryParameter> queryParameters = selectQuery.getSelectQueryParameters();

        String replacedQueryString = queryString;
        for (final QueryParameter queryParam : queryParameters) {
            final Object paramValue = queryParam.getValueToPrint();
            final String printedParam = InternalQueryPrinter.convertParameterToPrint(paramValue);
            replacedQueryString = replacedQueryString.replaceFirst("\\?", printedParam);
        }

        return replacedQueryString;

    }

    static String printInsertQuery(final InsertQueryInterface insertQuery) {

        final String queryString = insertQuery.getPreparedInsertQueryString();
        final List<QueryParameter> queryParameters = insertQuery.getInsertQueryParameters();

        String replacedQueryString = queryString;
        for (final QueryParameter queryParam : queryParameters) {
            final Object paramValue = queryParam.getValueToPrint();
            final String printedParam = InternalQueryPrinter.convertParameterToPrint(paramValue);
            replacedQueryString = replacedQueryString.replaceFirst("\\?", printedParam);
        }

        return replacedQueryString;

    }

    static private String convertParameterToPrint(final Object paramValue) {
        final String printedParam;
        if( paramValue instanceof String ) {
            printedParam = "'" + (String)paramValue + "'";
        }
        else
        if( paramValue instanceof Date ) {
            printedParam = "'" + paramValue.toString() + "'";
        }
        else {
            printedParam = paramValue.toString();
        }
        return printedParam;
    }

}// class
