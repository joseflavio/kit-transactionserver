package com.fap.framework.db;

public final class KitDataSourceSimple implements KitDataSource {

    private final DatabaseConfig dbConfig;

    public KitDataSourceSimple(final DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override
    public InsertQueryResult executeInsertQuery(final InsertQueryInterface insertQuery) {
        final InsertQueryResult result = InsertQueryExecuter.executeInsertQuery2(dbConfig, insertQuery);
        return result;
    }

    @Override
    public UpdateQueryResult executeUpdateQuery(final UpdateQueryInterface updateQuery) {
        final UpdateQueryResult result = UpdateQueryExecuter.executeUpdateQuery2(dbConfig, updateQuery);
        return result;
    }

    @Override
    public <T> SelectQueryResult<T> executeSelectQuery(final SelectQueryInterface selectQuery, final SelectQueryResultAdapter<T> queryResultAdapter) {
        final SelectQueryExecuter<T> selectQueryExecuter = new SelectQueryExecuter<T>(queryResultAdapter);
        final SelectQueryResult<T> result = selectQueryExecuter.executeSelectQuery2(dbConfig, selectQuery);
        return result;
    }

}// class
