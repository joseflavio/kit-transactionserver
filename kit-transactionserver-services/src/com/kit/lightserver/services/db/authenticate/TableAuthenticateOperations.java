package com.kit.lightserver.services.db.authenticate;

import java.util.List;

import com.kit.lightserver.services.be.authentication.AuthenticateQueryResult;
import com.kit.lightserver.services.db.SelectQueryResult;
import com.kit.lightserver.services.db.SelectQueryExecuter;
import com.kit.lightserver.services.db.UpdateQueryExecuter;
import com.kit.lightserver.services.db.UpdateQueryResult;

public final class TableAuthenticateOperations {

    static public SelectQueryResult<List<String>> selectLoggedInClients() {
        final SelectLoggedInClientIdsResultAdapter queryResultAdapter = new SelectLoggedInClientIdsResultAdapter();
        final SelectLoggedInClientIdsQuery selectQuery = new SelectLoggedInClientIdsQuery();
        final SelectQueryExecuter<List<String>> selectQueryExecuter = new SelectQueryExecuter<List<String>>(queryResultAdapter);
        final SelectQueryResult<List<String>> result = selectQueryExecuter.executeSelectQuery(selectQuery);
        return result;
    }

    public static UpdateQueryResult updateAllLoggedInClientsToLoggedOff() {
        final UpdateLoggedInUsersToLogOffQuery updateQuery = new UpdateLoggedInUsersToLogOffQuery();
        final UpdateQueryResult result = UpdateQueryExecuter.executeUpdateQuery(updateQuery);
        return result;
    }

    static public SelectQueryResult<AuthenticateQueryResult> selectClientIdExists(final String userKtClientId) {
        final SelectClientIdAndPasswordResultAdapter queryResultAdapter = new SelectClientIdAndPasswordResultAdapter();
        final SelectQueryExecuter<AuthenticateQueryResult> selectQueryExecuter = new SelectQueryExecuter<AuthenticateQueryResult>(queryResultAdapter);
        final SelectClientIdAndPasswordQuery selectQuery = new SelectClientIdAndPasswordQuery(userKtClientId);
        final SelectQueryResult<AuthenticateQueryResult> result = selectQueryExecuter.executeSelectQuery(selectQuery);
        return result;
    }

    static public UpdateQueryResult updateClientLoggedIn(final String ktClientId) {
        final UpdateAuthenticateUserLogInQuery updateQuery = new UpdateAuthenticateUserLogInQuery(ktClientId);
        final UpdateQueryResult result = UpdateQueryExecuter.executeUpdateQuery(updateQuery);
        return result;
    }

    public static UpdateQueryResult updateClientLoggedOff(final String ktClientId, final boolean mustResetInNextConnectio) {
        final UpdateAuthenticateUserLogOffQuery updateQuery = new UpdateAuthenticateUserLogOffQuery(ktClientId, mustResetInNextConnectio);
        final UpdateQueryResult result = UpdateQueryExecuter.executeUpdateQuery(updateQuery);
        return result;
    }



}// class
