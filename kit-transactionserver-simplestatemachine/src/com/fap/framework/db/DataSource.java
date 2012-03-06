package com.fap.framework.db;





public final class DataSource {

    private final DatabaseConfig dbConfig;

    public DataSource(final DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    public InsertQueryResult executeInsertQuery(final InsertQueryInterface insertQuery) {
        final InsertQueryResult result = InsertQueryExecuter.executeInsertQuery(dbConfig, insertQuery);
        return result;
    }

    public UpdateQueryResult executeUpdateQuery(final UpdateQueryInterface updateQuery) {
        final UpdateQueryResult result = UpdateQueryExecuter.executeUpdateQuery(dbConfig, updateQuery);
        return result;
    }

    public <T> SelectQueryResult<T> executeSelectQuery(final SelectQueryInterface selectQuery, final SelectQueryResultAdapter<T> queryResultAdapter) {
        final SelectQueryExecuter<T> selectQueryExecuter = new SelectQueryExecuter<T>(queryResultAdapter);
        final SelectQueryResult<T> result = selectQueryExecuter.executeSelectQuery(dbConfig, selectQuery);
        return result;
    }

}//class
