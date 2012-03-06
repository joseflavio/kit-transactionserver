package com.kit.lightserver.services.db.authenticate;

import com.fap.framework.db.DataSource;
import com.fap.framework.db.SelectQueryResult;
import com.fap.framework.db.UpdateQueryResult;
import com.kit.lightserver.services.be.authentication.AuthenticateQueryResult;

public final class TableAuthenticateOperations {

    private final DataSource dataSource;

    public TableAuthenticateOperations(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public SelectQueryResult<AuthenticateQueryResult> selectClientIdExists(final String userKtClientId) {
        SelectClientIdAndPasswordResultAdapter queryResultAdapter = new SelectClientIdAndPasswordResultAdapter();
        SelectAuthenticateByClientIdQuery selectQuery = new SelectAuthenticateByClientIdQuery(userKtClientId);
        SelectQueryResult<AuthenticateQueryResult> result = dataSource.executeSelectQuery(selectQuery, queryResultAdapter);
        return result;
    }

    public UpdateQueryResult updateClientLoggedIn(final String ktClientId) {
        UpdateAuthenticateUserLogInQuery updateQuery = new UpdateAuthenticateUserLogInQuery(ktClientId);
        UpdateQueryResult result = dataSource.executeUpdateQuery(updateQuery);
        return result;
    }

    public UpdateQueryResult updateClientLoggedOff(final String ktClientId, final boolean mustResetInNextConnectio) {
        UpdateAuthenticateUserLogOffQuery updateQuery = new UpdateAuthenticateUserLogOffQuery(ktClientId, mustResetInNextConnectio);
        UpdateQueryResult result = dataSource.executeUpdateQuery(updateQuery);
        return result;
    }

}// class
