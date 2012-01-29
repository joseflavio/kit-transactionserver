package com.kit.lightserver.services.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfap.chronometer.Chronometer;
import com.kit.lightserver.services.be.authentication.DatabaseConnectionUtil;
import com.kit.lightserver.services.db.logger.DatabaseLogger;

public final class SelectQueryExecuter<T> {

    static private final Logger LOGGER = LoggerFactory.getLogger(SelectQueryExecuter.class);

    private final SelectQueryResultAdapter<T> resultAdapter;

    public SelectQueryExecuter(final SelectQueryResultAdapter<T> resultAdapter) {
        this.resultAdapter = resultAdapter;
    }// constructor

    public SelectQueryResult<T> executeSelectQuery(final SelectQueryInterface selectQuery) {

        final Connection connection = DatabaseConnectionUtil.getInstance().getConnection();
        if (connection == null) {
            final SelectQueryResult<T> failResult = new SelectQueryResult<T>();
            return failResult;
        }

        /*
         * Log the query
         */
        DatabaseLogger.logSelectQuery(selectQuery);

        final Chronometer chronometer = new Chronometer("executeSelectQuery(..)");
        chronometer.start();
        final SelectQueryResult<T> result = executeSelectQuery(connection, selectQuery);
        chronometer.stop();

        DatabaseLogger.logSelectResult(chronometer, result);

        DatabaseConnectionUtil.getInstance().closeConnection(connection);

        return result;

    }

    private SelectQueryResult<T> executeSelectQuery(final Connection connection, final SelectQueryInterface selectQuery) {

        try {

            final String selectQueryStr = selectQuery.getPreparedSelectQueryString();
            final PreparedStatement st = connection.prepareStatement(selectQueryStr);
            final List<QueryParameter> selectQueryParameterList = selectQuery.getSelectQueryParameters();
            PreparedParametersUtil.fillParameters(st, selectQueryParameterList);
            final ResultSet rs = st.executeQuery();
            final T selectQueryResult = resultAdapter.adaptResultSet(rs);

            return new SelectQueryResult<T>(selectQueryResult);

        } catch (final SQLException e) {
            LOGGER.error("Error executing the query.", e);
            return new SelectQueryResult<T>();
        }

    }

}// class
