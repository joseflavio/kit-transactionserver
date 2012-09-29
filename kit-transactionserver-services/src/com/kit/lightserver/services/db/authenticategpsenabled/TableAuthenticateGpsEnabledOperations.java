package com.kit.lightserver.services.db.authenticategpsenabled;

import org.dajo.framework.db.InsertQueryResult;
import org.dajo.framework.db.QueryExecutor;
import org.dajo.framework.db.SelectQueryResult;
import org.dajo.framework.db.SelectQueryResultAdapterBoolean;
import org.dajo.framework.db.SelectQuerySingleResult;

public final class TableAuthenticateGpsEnabledOperations {

    private final QueryExecutor dbaQueryExecutor;

    public TableAuthenticateGpsEnabledOperations(final QueryExecutor dbaQueryExecutor) {
        this.dbaQueryExecutor = dbaQueryExecutor;
    }

    public SelectQueryResult<SelectQuerySingleResult<Boolean>> selectGpsEnabled(final String clientUserId) {
        SelectAuthenticateGpsEnabledQuery selectQuery = new SelectAuthenticateGpsEnabledQuery(clientUserId);
        SelectQueryResultAdapterBoolean rsAdapter = new SelectQueryResultAdapterBoolean();
        SelectQueryResult<SelectQuerySingleResult<Boolean>> result = dbaQueryExecutor.executeSelectQuery(selectQuery, rsAdapter);
        return result;
    }

    public InsertQueryResult firstInsertGpsEnabled(final String clientUserId) {
        InsertAuthenticateGpsEnabledQuery insertQuery = new InsertAuthenticateGpsEnabledQuery(clientUserId);
        InsertQueryResult result = dbaQueryExecutor.executeInsertQuery(insertQuery);
        return result;

    }

}// class
