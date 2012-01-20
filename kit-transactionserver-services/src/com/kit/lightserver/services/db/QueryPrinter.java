package com.kit.lightserver.services.db;

import java.util.List;

public final class QueryPrinter {

    private QueryPrinter() {
    }

    static public String printQuery(final UpdateQueryInterface updateQuery) {

        final String queryString = updateQuery.getPreparedUpdateQueryString();
        final List<QueryParameter> queryParameters = updateQuery.getUpdateQueryParameters();

        String replacedQueryString = queryString;
        for (final QueryParameter queryParam : queryParameters) {
            replacedQueryString = replacedQueryString.replaceFirst("\\?", queryParam.getValue().toString());
        }

        return replacedQueryString;

    }

    static public String printQuery(final SelectQueryInterface selectQuery) {

        final String queryString = selectQuery.getPreparedSelectQueryString();
        final List<QueryParameter> queryParameters = selectQuery.getSelectQueryParameters();

        String replacedQueryString = queryString;
        for (final QueryParameter queryParam : queryParameters) {
            replacedQueryString = replacedQueryString.replaceFirst("\\?", queryParam.getValue().toString());
        }

        return replacedQueryString;

    }

    static public String printQuery(final InsertQueryInterface insertQuery) {

        final String queryString = insertQuery.getPreparedInsertQueryString();
        final List<QueryParameter> queryParameters = insertQuery.getInsertQueryParameters();

        String replacedQueryString = queryString;
        for (final QueryParameter queryParam : queryParameters) {
            replacedQueryString = replacedQueryString.replaceFirst("\\?", queryParam.getValue().toString());
        }

        return replacedQueryString;

    }

}// class
