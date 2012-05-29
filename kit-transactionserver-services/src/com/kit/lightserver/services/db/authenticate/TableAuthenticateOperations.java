package com.kit.lightserver.services.db.authenticate;

import com.fap.framework.db.InsertQueryResult;
import com.fap.framework.db.KitDataSource;
import com.fap.framework.db.SelectQueryResult;
import com.fap.framework.db.SelectQueryResultAdapterBoolean;
import com.fap.framework.db.SelectQuerySingleResult;
import com.fap.framework.db.UpdateQueryResult;
import com.kit.lightserver.domain.types.ConnectionInfoVO;
import com.kit.lightserver.domain.types.InstallationIdAbVO;
import com.kit.lightserver.services.be.authentication.AuthenticateLastSuccessfulServiceResponse;
import com.kit.lightserver.services.be.authentication.AuthenticateQueryResult;

public final class TableAuthenticateOperations {

    private final KitDataSource dataSource;

    public TableAuthenticateOperations(final KitDataSource dataSource) {
        this.dataSource = dataSource;
    }

    private final SelectAuthenticatePasswordQueryResultAdapter queryResultAdapter = new SelectAuthenticatePasswordQueryResultAdapter();

    private final SelectQueryResultAdapterBoolean selectMustResetResultAdapter = new SelectQueryResultAdapterBoolean();

    private final SelectAuthenticateUltimoSucessoResultAdapter selectLastSuccessAuthenticationAdapter = new SelectAuthenticateUltimoSucessoResultAdapter();

    public SelectQueryResult<AuthenticateQueryResult> selectClientIdExists(final String clientUserId) {
        SelectAuthenticatePasswordQuery selectQuery = new SelectAuthenticatePasswordQuery(clientUserId);
        SelectQueryResult<AuthenticateQueryResult> result = dataSource.executeSelectQuery(selectQuery, queryResultAdapter);
        return result;
    }

    public SelectQueryResult<SelectQuerySingleResult<Boolean>> selectMustReset(final String clientUserId) {
        SelectAuthenticateDeveResetarQuery selectQuery = new SelectAuthenticateDeveResetarQuery(clientUserId);
        SelectQueryResult<SelectQuerySingleResult<Boolean>> result = dataSource.executeSelectQuery(selectQuery, selectMustResetResultAdapter);
        return result;
    }

    public InsertQueryResult firstInsertMustReset(final String clientUserId) {
        InsertAuthenticateDeveResetarQuery insertQuery = new InsertAuthenticateDeveResetarQuery(clientUserId);
        InsertQueryResult result = dataSource.executeInsertQuery(insertQuery);
        return result;
    }

    public UpdateQueryResult updateMustReset(final String clientUserId, final boolean mustResetInNextConnection) {
        UpdateAuthenticateDeveResetarQuery updateQuery = new UpdateAuthenticateDeveResetarQuery(clientUserId, mustResetInNextConnection);
        UpdateQueryResult result = dataSource.executeUpdateQuery(updateQuery);
        return result;
    }

    public SelectQueryResult<SelectAuthenticateUltimoSucessoResult> selectLastSuccessAuthentication(final String clientUserId) {
        SelectAuthenticateUltimoSucessoQuery selectQuery = new SelectAuthenticateUltimoSucessoQuery(clientUserId);
        SelectQueryResult<SelectAuthenticateUltimoSucessoResult> result = dataSource.executeSelectQuery(selectQuery, selectLastSuccessAuthenticationAdapter);
        return result;
    }

    public InsertQueryResult firstInsertLastConnection(final String clientUserId, final InstallationIdAbVO installationIdSTY, final ConnectionInfoVO connectionInfo) {
        InsertAuthenticateUltimaConexaoQuery insertQuery = new InsertAuthenticateUltimaConexaoQuery(clientUserId, installationIdSTY, connectionInfo);
        InsertQueryResult result = dataSource.executeInsertQuery(insertQuery);
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

        UpdateQueryResult result = dataSource.executeUpdateQuery(updateQuery);

        return result;

    }

}// class
