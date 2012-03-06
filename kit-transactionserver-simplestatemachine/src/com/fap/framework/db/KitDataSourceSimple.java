package com.fap.framework.db;

public final class KitDataSourceSimple implements KitDataSource {

    private final DatabaseConfig dbConfig;

    public KitDataSourceSimple(final DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override
    public InsertQueryResult executeInsertQuery(final InsertQueryInterface insertQuery) {
        final InsertQueryResult result = InsertQueryExecuter.executeInsertQuery(dbConfig, insertQuery);
        return result;
    }

    @Override
    public UpdateQueryResult executeUpdateQuery(final UpdateQueryInterface updateQuery) {
        final UpdateQueryResult result = UpdateQueryExecuter.executeUpdateQuery(dbConfig, updateQuery);
        return result;
    }

    @Override
    public <T> SelectQueryResult<T> executeSelectQuery(final SelectQueryInterface selectQuery, final SelectQueryResultAdapter<T> queryResultAdapter) {
        final SelectQueryExecuter<T> selectQueryExecuter = new SelectQueryExecuter<T>(queryResultAdapter);
        final SelectQueryResult<T> result = selectQueryExecuter.executeSelectQuery(dbConfig, selectQuery);
        return result;
    }

}// class
