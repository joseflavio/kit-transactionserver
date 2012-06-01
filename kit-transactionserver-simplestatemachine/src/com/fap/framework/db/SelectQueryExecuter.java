package com.fap.framework.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class SelectQueryExecuter {

    static private final Logger LOGGER = LoggerFactory.getLogger(SelectQueryExecuter.class);

    static <T> SelectQueryResult<T> executeSelectQuery(
            final Connection connection, final SelectQueryInterface selectQuery, final SelectQueryResultAdapter<T> resultAdapter) {

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
            LOGGER.error("Error executing the query. selectQuery={}", new SelectQueryPrinter(selectQuery), e);
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

        return result;

    }

}// class
