package com.kit.lightserver.services.db.authenticate;

import org.dajo.framework.db.InsertQueryResult;
import org.dajo.framework.db.QueryExecutor;
import org.dajo.framework.db.SelectQueryResult;
import org.dajo.framework.db.SelectQueryResultAdapterBoolean;
import org.dajo.framework.db.SelectQuerySingleResult;
import org.dajo.framework.db.UpdateQueryResult;

import com.kit.lightserver.domain.types.ConnectionInfoVO;
import com.kit.lightserver.domain.types.InstallationIdAbVO;
import com.kit.lightserver.services.be.authentication.AuthenticateLastSuccessfulServiceResponse;
import com.kit.lightserver.services.be.authentication.AuthenticateQueryResult;

public final class TableAuthenticateOperations {

    private final QueryExecutor dbaQueryExecutor;
    private final QueryExecutor dbdQueryExecutor;

    public TableAuthenticateOperations(final QueryExecutor dbaQueryExecutor, final QueryExecutor dbdQueryExecutor) {
        this.dbaQueryExecutor = dbaQueryExecutor;
        this.dbdQueryExecutor = dbdQueryExecutor;
    }

    private final SelectAuthenticatePasswordQueryResultAdapter queryResultAdapter = new SelectAuthenticatePasswordQueryResultAdapter();

    private final SelectAuthenticateUltimoSucessoResultAdapter selectLastSuccessAuthenticationAdapter = new SelectAuthenticateUltimoSucessoResultAdapter();

    public SelectQueryResult<AuthenticateQueryResult> selectClientIdExists(final String clientUserId) {
        SelectAuthenticatePasswordQuery selectQuery = new SelectAuthenticatePasswordQuery(clientUserId);
        SelectQueryResult<AuthenticateQueryResult> result = dbaQueryExecutor.executeSelectQuery(selectQuery, queryResultAdapter);
        return result;
    }

    public SelectQueryResult<SelectQuerySingleResult<Boolean>> selectMustReset(final String clientUserId) {
        SelectAuthenticateDeveResetarQuery selectQuery = new SelectAuthenticateDeveResetarQuery(clientUserId);
        SelectQueryResultAdapterBoolean rsAdapter = new SelectQueryResultAdapterBoolean();
        SelectQueryResult<SelectQuerySingleResult<Boolean>> result = dbdQueryExecutor.executeSelectQuery(selectQuery, rsAdapter);
        return result;
    }

    public InsertQueryResult firstInsertMustReset(final String clientUserId) {
        InsertAuthenticateDeveResetarQuery insertQuery = new InsertAuthenticateDeveResetarQuery(clientUserId);
        InsertQueryResult result = dbdQueryExecutor.executeInsertQuery(insertQuery);
        return result;
    }



    public SelectQueryResult<SelectAuthenticateUltimoSucessoResult> selectLastSuccessAuthentication(final String clientUserId) {
        SelectAuthenticateUltimoSucessoQuery selectQuery = new SelectAuthenticateUltimoSucessoQuery(clientUserId);
        SelectQueryResult<SelectAuthenticateUltimoSucessoResult> result = dbdQueryExecutor.executeSelectQuery(selectQuery, selectLastSuccessAuthenticationAdapter);
        return result;
    }

    public InsertQueryResult firstInsertLastConnection(final String clientUserId, final InstallationIdAbVO installationIdSTY, final ConnectionInfoVO connectionInfo) {
        InsertAuthenticateUltimaConexaoQuery insertQuery = new InsertAuthenticateUltimaConexaoQuery(clientUserId, installationIdSTY, connectionInfo);
        InsertQueryResult result = dbdQueryExecutor.executeInsertQuery(insertQuery);
        return result;
    }

    public UpdateQueryResult updateAuthUltimoSucesso(
            final InstallationIdAbVO newInstallationIdAb, final ConnectionInfoVO newConnectionInfo,
            final String clientUserId, final AuthenticateLastSuccessfulServiceResponse lastResponse) {

        final String newConnectionUniqueId = newConnectionInfo.getConnectionUniqueId();

        final InstallationIdAbVO lastInstallationIdAb = lastResponse.getLastInstallationIdAb();
        final String lastConnectionUniqueId = lastResponse.getLastConnectionUniqueId();
        final int lastVersion = lastResponse.getLastVersion();

        UpdateAuthenticateUltimoSucessoQuery updateQuery = new UpdateAuthenticateUltimoSucessoQuery(
                newInstallationIdAb, newConnectionUniqueId, clientUserId, lastInstallationIdAb, lastConnectionUniqueId, lastVersion);

        UpdateQueryResult result = dbdQueryExecutor.executeUpdateQuery(updateQuery);

        return result;

    }

}// class
