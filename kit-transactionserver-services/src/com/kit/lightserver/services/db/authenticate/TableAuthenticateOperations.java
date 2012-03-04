package com.kit.lightserver.services.db.authenticate;

import com.fap.framework.db.DatabaseConfig;
import com.fap.framework.db.SelectQueryExecuter;
import com.fap.framework.db.SelectQueryResult;
import com.fap.framework.db.UpdateQueryExecuter;
import com.fap.framework.db.UpdateQueryResult;
import com.kit.lightserver.services.be.authentication.AuthenticateQueryResult;

public final class TableAuthenticateOperations {

    private final DatabaseConfig dbConfig;

    public TableAuthenticateOperations(final DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    public SelectQueryResult<AuthenticateQueryResult> selectClientIdExists(final String userKtClientId) {
        final SelectClientIdAndPasswordResultAdapter queryResultAdapter = new SelectClientIdAndPasswordResultAdapter();
        final SelectQueryExecuter<AuthenticateQueryResult> selectQueryExecuter = new SelectQueryExecuter<AuthenticateQueryResult>(queryResultAdapter);
        final SelectAuthenticateByClientIdQuery selectQuery = new SelectAuthenticateByClientIdQuery(userKtClientId);
        final SelectQueryResult<AuthenticateQueryResult> result = selectQueryExecuter.executeSelectQuery(dbConfig, selectQuery);
        return result;
    }

    public UpdateQueryResult updateClientLoggedIn(final String ktClientId) {
        final UpdateAuthenticateUserLogInQuery updateQuery = new UpdateAuthenticateUserLogInQuery(ktClientId);
        final UpdateQueryResult result = UpdateQueryExecuter.executeUpdateQuery(dbConfig, updateQuery);
        return result;
    }

    public UpdateQueryResult updateClientLoggedOff(final String ktClientId, final boolean mustResetInNextConnectio) {
        final UpdateAuthenticateUserLogOffQuery updateQuery = new UpdateAuthenticateUserLogOffQuery(ktClientId, mustResetInNextConnectio);
        final UpdateQueryResult result = UpdateQueryExecuter.executeUpdateQuery(dbConfig, updateQuery);
        return result;
    }

}// class
