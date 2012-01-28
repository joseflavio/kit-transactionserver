package com.kit.lightserver.services.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfap.chronometer.Chronometer;
import com.kit.lightserver.services.be.authentication.DatabaseConnectionUtil;

public final class UpdateQueryExecuter {

    static private final Logger LOGGER = LoggerFactory.getLogger(UpdateQueryExecuter.class);

    static public UpdateQueryResult executeUpdateQuery(final UpdateQueryInterface updateQuery) {

        final Connection connection = DatabaseConnectionUtil.getInstance().getConnection();
        if (connection == null) {
            final UpdateQueryResult failResult = new UpdateQueryResult();
            return failResult;
        }

        final String printedUpdateQuery = QueryPrinter.printQuery(updateQuery);
        LOGGER.info("Executing query. printedUpdateQuery=" + printedUpdateQuery);

        final Chronometer chronometer = new Chronometer("UpdateQueryExecuter.executeUpdateQuery(..)");
        chronometer.start();
        final UpdateQueryResult result = UpdateQueryExecuter.executeUpdateQuery(connection, updateQuery);
        chronometer.stop();

        DatabaseConnectionUtil.getInstance().closeConnection(connection);

        LOGGER.info(chronometer.getLogString());

        return result;

    }

    static private UpdateQueryResult executeUpdateQuery(final Connection databaseConnection, final UpdateQueryInterface updateQuery) {

        try {

            final String updateQueryStr = updateQuery.getPreparedUpdateQueryString();
            final PreparedStatement st = databaseConnection.prepareStatement(updateQueryStr);
            final List<QueryParameter> updateQueryParameterList = updateQuery.getUpdateQueryParameters();
            PreparedParametersUtil.fillParameters(st, updateQueryParameterList);

            final int rowsUpdated = st.executeUpdate();

            return new UpdateQueryResult(rowsUpdated);

        }
        catch (final SQLException e) {
            LOGGER.error("Error executing the query.", e);
            return new UpdateQueryResult();
        }
        catch (final Exception e) {
            LOGGER.error("Unexpected error executing the query.", e);
            return new UpdateQueryResult();
        }

    }

}// class
