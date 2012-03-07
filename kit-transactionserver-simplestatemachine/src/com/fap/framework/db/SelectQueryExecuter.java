package com.fap.framework.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fap.chronometer.Chronometer;
import com.fap.loggers.db.DatabaseLogger;

public final class SelectQueryExecuter<T> {

    static private final Logger LOGGER = LoggerFactory.getLogger(SelectQueryExecuter.class);

    private final SelectQueryResultAdapter<T> resultAdapter;

    public SelectQueryExecuter(final SelectQueryResultAdapter<T> resultAdapter) {
        this.resultAdapter = resultAdapter;
    }// constructor

    public SelectQueryResult<T> executeSelectQuery2(final DatabaseConfig dbConfig, final SelectQueryInterface selectQuery) {

        final Connection connection = DatabaseConnectionUtil.getInstance2().getConnection(dbConfig);
        if (connection == null) {
            final SelectQueryResult<T> failResult = new SelectQueryResult<T>();
            return failResult;
        }

        final SelectQueryResult<T> result = executeSelectQuery(connection, selectQuery);

        DatabaseConnectionUtil.getInstance2().closeConnection(connection);

        return result;

    }

    private SelectQueryResult<T> executeSelectQuery(final Connection connection, final SelectQueryInterface selectQuery) {

        /*
         * Log the query
         */
        DatabaseLogger.logSelectQuery(selectQuery);

        final Chronometer chronometer = new Chronometer("executeSelectQuery(..)");
        chronometer.start();

        SelectQueryResult<T> result;
        ResultSet rs = null;
        try {

            String selectQueryStr = selectQuery.getPreparedSelectQueryString();
            PreparedStatement st = connection.prepareStatement(selectQueryStr);
            List<QueryParameter> selectQueryParameterList = selectQuery.getSelectQueryParameters();
            PreparedParametersUtil.fillParameters(st, selectQueryParameterList);
            rs = st.executeQuery();

            final T selectQueryResult = resultAdapter.adaptResultSet(rs);
            result = new SelectQueryResult<T>(selectQueryResult);
            rs.close();

        } catch (final SQLException e) {
            LOGGER.error("Error executing the query.", e);
            result = new SelectQueryResult<T>();
        } finally {

            if( rs != null ) {
                try {
                    rs.close();
                }
                catch (SQLException e) {
                    LOGGER.error("Error closing the ResultSet.", e);
                }
            }
        }

        chronometer.stop();
        DatabaseLogger.logSelectResult(chronometer, result);

        return result;

    }

}// class
