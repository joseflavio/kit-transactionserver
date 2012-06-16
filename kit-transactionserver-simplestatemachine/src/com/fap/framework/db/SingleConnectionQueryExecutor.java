package com.fap.framework.db;

import java.sql.Connection;

public final class SingleConnectionQueryExecutor implements QueryExecutor {

    private Connection connection;

    public SingleConnectionQueryExecutor(final DatabaseConfig dbConfig) {
        this.connection = DatabaseConnectionUtil.getInstance().getConnection(dbConfig);
    }

    public void finish() {
        if( connection != null ) {
            DatabaseConnectionUtil.getInstance().closeConnection(connection);
            connection = null;
        }
    }

    @Override
    protected void finalize() {
        if( connection != null ) {
            DatabaseConnectionUtil.getInstance().closeConnection(connection);
        }
    }

    @Override
    public InsertQueryResult executeInsertQuery(final InsertQueryInterface insertQuery) {
        if( connection == null ) {
            return new InsertQueryResult();
        }
        final InsertQueryResult result = InsertQueryExecuter.executeInsertQuery(connection, insertQuery);
        return result;
    }

    @Override
    public UpdateQueryResult executeUpdateQuery(final UpdateQueryInterface updateQuery) {
        if( connection == null ) {
            return new UpdateQueryResult();
        }
        final UpdateQueryResult result = UpdateQueryExecuter.executeUpdateQuery(connection, updateQuery);
        return result;
    }

    @Override
    public <T> SelectQueryResult<T> executeSelectQuery(final SelectQueryInterface selectQuery, final SelectQueryResultAdapter<T> queryResultAdapter) {
        if( connection == null ) {
            return new SelectQueryResult<T>();
        }
        final SelectQueryResult<T> result = SelectQueryExecuter.executeSelectQuery(connection, selectQuery, queryResultAdapter);
        return result;
    }

}// class
