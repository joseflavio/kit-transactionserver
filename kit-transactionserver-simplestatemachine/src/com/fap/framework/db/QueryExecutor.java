package com.fap.framework.db;


public interface QueryExecutor {

    InsertQueryResult executeInsertQuery(InsertQueryInterface insertQuery);

    UpdateQueryResult executeUpdateQuery(UpdateQueryInterface updateQuery);

    <T> SelectQueryResult<T> executeSelectQuery(SelectQueryInterface selectQuery, SelectQueryResultAdapter<T> queryResultAdapter);

}//class
