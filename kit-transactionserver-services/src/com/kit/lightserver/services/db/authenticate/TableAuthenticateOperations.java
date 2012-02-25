package com.kit.lightserver.services.db.authenticate;

import com.kit.lightserver.services.be.authentication.AuthenticateQueryResult;
import com.kit.lightserver.services.be.authentication.DatabaseConfig;
import com.kit.lightserver.services.db.SelectQueryExecuter;
import com.kit.lightserver.services.db.SelectQueryResult;
import com.kit.lightserver.services.db.UpdateQueryExecuter;
import com.kit.lightserver.services.db.UpdateQueryResult;

public final class TableAuthenticateOperations {

    private final DatabaseConfig dbConfig;

    public TableAuthenticateOperations(final DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    public SelectQueryResult<AuthenticateQueryResult> selectClientIdExists(final String userKtClientId) {
        final SelectClientIdAndPasswordResultAdapter queryResultAdapter = new SelectClientIdAndPasswordResultAdapter();
        final SelectQueryExecuter<AuthenticateQueryResult> selectQueryExecuter = new SelectQueryExecuter<AuthenticateQueryResult>(queryResultAdapter);
        final SelectClientIdAndPasswordQuery selectQuery = new SelectClientIdAndPasswordQuery(userKtClientId);
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
