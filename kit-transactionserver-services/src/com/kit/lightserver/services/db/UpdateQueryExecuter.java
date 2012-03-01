package com.kit.lightserver.services.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fap.chronometer.Chronometer;
import com.fap.framework.db.DatabaseConfig;
import com.fap.framework.db.DatabaseConnectionUtil;
import com.fap.framework.db.QueryParameter;
import com.fap.framework.db.UpdateQueryInterface;
import com.fap.framework.db.UpdateQueryResult;
import com.fap.loggers.db.DatabaseLogger;

public final class UpdateQueryExecuter {

    static private final Logger LOGGER = LoggerFactory.getLogger(UpdateQueryExecuter.class);

    static public UpdateQueryResult executeUpdateQuery(final DatabaseConfig dbConfig, final UpdateQueryInterface updateQuery) {

        final Connection connection = DatabaseConnectionUtil.getInstance2().getConnection(dbConfig);
        if (connection == null) {
            final UpdateQueryResult failResult = new UpdateQueryResult();
            return failResult;
        }

        DatabaseLogger.logUpdateQuery(updateQuery);

        final Chronometer chronometer = new Chronometer("executeUpdateQuery(..)");
        chronometer.start();
        final UpdateQueryResult result = UpdateQueryExecuter.executeUpdateQuery(connection, updateQuery);
        chronometer.stop();

        DatabaseLogger.logUpdateResult(chronometer, result);

        DatabaseConnectionUtil.getInstance2().closeConnection(connection);

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
