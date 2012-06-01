package com.fap.framework.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class UpdateQueryExecuter {

    static private final Logger LOGGER = LoggerFactory.getLogger(UpdateQueryExecuter.class);

    static UpdateQueryResult executeUpdateQuery(final Connection databaseConnection, final UpdateQueryInterface updateQuery) {

        try {

            final String updateQueryStr = updateQuery.getPreparedUpdateQueryString();
            final PreparedStatement st = databaseConnection.prepareStatement(updateQueryStr);
            final List<QueryParameter> updateQueryParameterList = updateQuery.getUpdateQueryParameters();
            PreparedParametersUtil.fillParameters(st, updateQueryParameterList);

            final int rowsUpdated = st.executeUpdate();

            return new UpdateQueryResult(rowsUpdated);

        }
        catch (final SQLException e) {
            LOGGER.error("Error executing the query. updateQuery={}", new UpdateQueryPrinter(updateQuery), e);
            return new UpdateQueryResult();
        }
        catch (final Exception e) {
            LOGGER.error("Unexpected error executing the query.", e);
            return new UpdateQueryResult();
        }

    }

}// class
