package com.kit.lightserver.services.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kit.lightserver.services.be.authentication.DatabaseConnectionUtil;

public final class SelectQueryExecuter<T> {

    static private final Logger LOGGER = LoggerFactory.getLogger(SelectQueryExecuter.class);

    private final SelectQueryResultAdapter<T> resultAdapter;

    public SelectQueryExecuter(final SelectQueryResultAdapter<T> resultAdapter) {
        this.resultAdapter = resultAdapter;
    }// constructor

    public QueryResultContainer<T> executeSelectQuery(final SelectQueryInterface selectQuery) {

        final Connection connection = DatabaseConnectionUtil.getConnection();
        if (connection == null) {
            final QueryResultContainer<T> failResult = new QueryResultContainer<T>();
            return failResult;
        }

        final String queryName = selectQuery.getClass().getCanonicalName();
        final String printedSelectQuery = QueryPrinter.printQuery(selectQuery);
        LOGGER.info("Executing query. queryName="+ queryName + ", printedSelectQuery=" + printedSelectQuery);

        final long startTime = System.nanoTime();
        final QueryResultContainer<T> result = executeSelectQuery(connection, selectQuery);
        final long endTime = System.nanoTime();

        DatabaseConnectionUtil.closeConnection(connection);

        final double elapsedTime = (endTime - startTime) / 1000000000.0d;
        LOGGER.info("Query executed. elapsedTime="+elapsedTime+"s, result=" + result);
        return result;
    }

    private QueryResultContainer<T> executeSelectQuery(final Connection connection, final SelectQueryInterface selectQuery) {

        try {

            final String selectQueryStr = selectQuery.getPreparedSelectQueryString();
            final PreparedStatement st = connection.prepareStatement(selectQueryStr);
            final List<QueryParameter> selectQueryParameterList = selectQuery.getSelectQueryParameters();
            PreparedParametersUtil.fillParameters(st, selectQueryParameterList);
            final ResultSet rs = st.executeQuery();
            final T selectQueryResult = resultAdapter.adaptResultSet(rs);

            return new QueryResultContainer<T>(selectQueryResult);

        } catch (final SQLException e) {
            LOGGER.error("Error executing the query.", e);
            return new QueryResultContainer<T>();
        }
    }

}// class
