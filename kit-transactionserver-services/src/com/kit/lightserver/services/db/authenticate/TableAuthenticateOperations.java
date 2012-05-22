package com.kit.lightserver.services.db.authenticate;

import com.fap.framework.db.InsertQueryResult;
import com.fap.framework.db.KitDataSource;
import com.fap.framework.db.SelectQueryResult;
import com.fap.framework.db.SelectQueryResultAdapterBoolean;
import com.fap.framework.db.SelectQueryResultSingleBoolean;
import com.fap.framework.db.UpdateQueryResult;
import com.kit.lightserver.services.be.authentication.AuthenticateQueryResult;

public final class TableAuthenticateOperations {

    private final KitDataSource dataSource;

    public TableAuthenticateOperations(final KitDataSource dataSource) {
        this.dataSource = dataSource;
    }

    private final SelectClientIdAndPasswordResultAdapter queryResultAdapter = new SelectClientIdAndPasswordResultAdapter();

    private final SelectQueryResultAdapterBoolean selectMustResetResultAdapter = new SelectQueryResultAdapterBoolean();

    public SelectQueryResult<AuthenticateQueryResult> selectClientIdExists(final String userClientId) {
        SelectAuthenticateByClientIdQuery selectQuery = new SelectAuthenticateByClientIdQuery(userClientId);
        SelectQueryResult<AuthenticateQueryResult> result = dataSource.executeSelectQuery(selectQuery, queryResultAdapter);
        return result;
    }

    public SelectQueryResult<SelectQueryResultSingleBoolean> selectMustReset(final String userClientId) {
        SelectAuthenticateDeveResetarQuery selectQuery = new SelectAuthenticateDeveResetarQuery(userClientId);
        SelectQueryResult<SelectQueryResultSingleBoolean> result = dataSource.executeSelectQuery(selectQuery, selectMustResetResultAdapter);
        return result;
    }

    public InsertQueryResult insertMustReset(final String userClientId) {
        InsertAuthenticateDeveResetarQuery insertQuery = new InsertAuthenticateDeveResetarQuery(userClientId);
        InsertQueryResult result = dataSource.executeInsertQuery(insertQuery);
        return result;
    }

    public UpdateQueryResult updateClientLoggedIn(final String userClientId) {
        UpdateAuthenticateUserLogInQuery updateQuery = new UpdateAuthenticateUserLogInQuery(userClientId);
        UpdateQueryResult result = dataSource.executeUpdateQuery(updateQuery);
        return result;
    }

    public UpdateQueryResult updateMustReset(final String userClientId, final boolean mustResetInNextConnection) {
        UpdateAuthenticateDeveResetarQuery updateQuery = new UpdateAuthenticateDeveResetarQuery(userClientId, mustResetInNextConnection);
        UpdateQueryResult result = dataSource.executeUpdateQuery(updateQuery);
        return result;
    }

    public UpdateQueryResult updateLastDisconnection(final String userClientId) {
        UpdateAuthenticateLastDisconnectionQuery updateQuery = new UpdateAuthenticateLastDisconnectionQuery(userClientId);
        UpdateQueryResult result = dataSource.executeUpdateQuery(updateQuery);
        return result;
    }

}// class
