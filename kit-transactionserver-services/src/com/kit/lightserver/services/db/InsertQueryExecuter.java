package com.kit.lightserver.services.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kit.lightserver.services.be.authentication.DatabaseConnectionUtil;

public final class InsertQueryExecuter {

    static private final Logger LOGGER = LoggerFactory.getLogger(InsertQueryExecuter.class);

    static public InsertQueryResult executeInsertQuery(final InsertQueryInterface insertQuery) {

        final Connection connection = DatabaseConnectionUtil.getInstance().getConnection();
        if (connection == null) {
            final InsertQueryResult failResult = new InsertQueryResult();
            return failResult;
        }

        final String printedInsertQuery = QueryPrinter.printQuery(insertQuery);
        LOGGER.info("Executing query. printedUpdateQuery=" + printedInsertQuery);

        final InsertQueryResult result = InsertQueryExecuter.executeInsertQuery(connection, insertQuery);
        LOGGER.info("result=" + result);

        DatabaseConnectionUtil.getInstance().closeConnection(connection);

        return result;

    }

    static private InsertQueryResult executeInsertQuery(final Connection databaseConnection, final InsertQueryInterface updateQuery) {

        try {

            final String insertQueryStr = updateQuery.getPreparedInsertQueryString();
            final PreparedStatement st = databaseConnection.prepareStatement(insertQueryStr);
            final List<QueryParameter> insertQueryParameterList = updateQuery.getInsertQueryParameters();
            PreparedParametersUtil.fillParameters(st, insertQueryParameterList);

            final int rowsInserted = st.executeUpdate();
            if (rowsInserted == 1) {
                return new InsertQueryResult(rowsInserted);
            }
            else {
                LOGGER.error("Expected to insert a single row in the database. rowsUpdated=" + rowsInserted);
                return new InsertQueryResult();
            }

        }
        catch (final SQLException e) {
            LOGGER.error("Error executing the query.", e);
            return new InsertQueryResult();
        }// try-catch

    }

}// class
