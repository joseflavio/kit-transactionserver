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

    private final SelectClientIdAndPasswordResultAdapter queryResultAdapter = new SelectClientIdAndPasswordResultAdapter();

    private final SelectQueryResultAdapterBoolean selectMustResetResultAdapter = new SelectQueryResultAdapterBoolean();

    private final SelectAuthenticateLastSuccessResultAdapter selectLastSuccessAuthenticationAdapter = new SelectAuthenticateLastSuccessResultAdapter();

    public SelectQueryResult<AuthenticateQueryResult> selectClientIdExists(final String userClientId) {
        SelectAuthenticateByClientIdQuery selectQuery = new SelectAuthenticateByClientIdQuery(userClientId);
        SelectQueryResult<AuthenticateQueryResult> result = dataSource.executeSelectQuery(selectQuery, queryResultAdapter);
        return result;
    }

    public SelectQueryResult<SelectQuerySingleResult<Boolean>> selectMustReset(final String userClientId) {
        SelectAuthenticateDeveResetarQuery selectQuery = new SelectAuthenticateDeveResetarQuery(userClientId);
        SelectQueryResult<SelectQuerySingleResult<Boolean>> result = dataSource.executeSelectQuery(selectQuery, selectMustResetResultAdapter);
        return result;
    }

    public InsertQueryResult firstInsertMustReset(final String userClientId) {
        InsertAuthenticateDeveResetarQuery insertQuery = new InsertAuthenticateDeveResetarQuery(userClientId);
        InsertQueryResult result = dataSource.executeInsertQuery(insertQuery);
        return result;
    }

    public UpdateQueryResult updateMustReset(final String userClientId, final boolean mustResetInNextConnection) {
        UpdateAuthenticateDeveResetarQuery updateQuery = new UpdateAuthenticateDeveResetarQuery(userClientId, mustResetInNextConnection);
        UpdateQueryResult result = dataSource.executeUpdateQuery(updateQuery);
        return result;
    }

    public SelectQueryResult<SelectAuthenticateLastSuccessResult> selectLastSuccessAuthentication(final String userClientId) {
        SelectAuthenticateLastSuccessQuery selectQuery = new SelectAuthenticateLastSuccessQuery(userClientId);
        SelectQueryResult<SelectAuthenticateLastSuccessResult> result = dataSource.executeSelectQuery(selectQuery, selectLastSuccessAuthenticationAdapter);
        return result;
    }

    public InsertQueryResult firstInsertLastConnection(final String userClientId, final InstallationIdAbVO installationIdSTY, final ConnectionInfoVO connectionInfo) {
        InsertAuthenticateUltimaConexaoQuery insertQuery = new InsertAuthenticateUltimaConexaoQuery(userClientId, installationIdSTY, connectionInfo);
        InsertQueryResult result = dataSource.executeInsertQuery(insertQuery);
        return result;
    }

    public UpdateQueryResult updateAuthUltimoSucesso(
            final InstallationIdAbVO newInstallationIdAb, final ConnectionInfoVO newConnectionInfo,
            final String userClientId, final AuthenticateLastSuccessfulServiceResponse lastResponse) {

        final String newConnectionUniqueId = newConnectionInfo.getConnectionUniqueId();

        final InstallationIdAbVO lastInstallationIdAb = lastResponse.getLastInstallationIdAb();
        final String lastConnectionUniqueId = lastResponse.getLastConnectionUniqueId();
        final int lastVersion = lastResponse.getLastVersion();

        UpdateAuthenticateUltimoSucessoQuery updateQuery = new UpdateAuthenticateUltimoSucessoQuery(
                newInstallationIdAb, newConnectionUniqueId, userClientId, lastInstallationIdAb, lastConnectionUniqueId, lastVersion);

        UpdateQueryResult result = dataSource.executeUpdateQuery(updateQuery);

        return result;

    }


    public UpdateQueryResult updateLastDisconnection(final String userClientId) {
        UpdateAuthenticateLastDisconnectionQuery updateQuery = new UpdateAuthenticateLastDisconnectionQuery(userClientId);
        UpdateQueryResult result = dataSource.executeUpdateQuery(updateQuery);
        return result;
    }

}// class
