package com.fap.framework.db;

import java.sql.Connection;

import com.fap.chronometer.Chronometer;
import com.fap.loggers.db.DatabaseLogger;

public final class SimpleQueryExecutor implements QueryExecutor {

    private final DatabaseConfig dbConfig;

    public SimpleQueryExecutor(final DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override
    public InsertQueryResult executeInsertQuery(final InsertQueryInterface insertQuery) {

        DatabaseLogger.logInsertQuery(insertQuery);

        final Chronometer chronometer = new Chronometer("executeInsertQuery(..)");
        chronometer.start();

        final Connection connection = DatabaseConnectionUtil.getInstance().getConnection(dbConfig);
        if (connection == null) {
            return new InsertQueryResult();
        }

        final InsertQueryResult result = InsertQueryExecuter.executeInsertQuery(connection, insertQuery);

        DatabaseConnectionUtil.getInstance().closeConnection(connection);

        chronometer.stop();

        DatabaseLogger.logInsertResult(chronometer, result);
        return result;

    }

    @Override
    public UpdateQueryResult executeUpdateQuery(final UpdateQueryInterface updateQuery) {

        DatabaseLogger.logUpdateQuery(updateQuery);

        final Chronometer chronometer = new Chronometer("executeUpdateQuery(..)");
        chronometer.start();

        final Connection connection = DatabaseConnectionUtil.getInstance().getConnection(dbConfig);
        if (connection == null) {
            return new UpdateQueryResult();
        }

        final UpdateQueryResult result = UpdateQueryExecuter.executeUpdateQuery(connection, updateQuery);

        DatabaseConnectionUtil.getInstance().closeConnection(connection);

        chronometer.stop();

        DatabaseLogger.logUpdateResult(chronometer, result);
        return result;

    }

    @Override
    public <T> SelectQueryResult<T> executeSelectQuery(final SelectQueryInterface selectQuery, final SelectQueryResultAdapter<T> queryResultAdapter) {

        DatabaseLogger.logSelectQuery(selectQuery);

        final Chronometer chronometer = new Chronometer("executeSelectQuery(..)");
        chronometer.start();

        final Connection connection = DatabaseConnectionUtil.getInstance().getConnection(dbConfig);
        if (connection == null) {
            return new SelectQueryResult<T>();
        }

        final SelectQueryResult<T> result = SelectQueryExecuter.executeSelectQuery(connection, selectQuery, queryResultAdapter);

        DatabaseConnectionUtil.getInstance().closeConnection(connection);

        chronometer.stop();

        DatabaseLogger.logSelectResult(chronometer, result);
        return result;

    }

}// class
