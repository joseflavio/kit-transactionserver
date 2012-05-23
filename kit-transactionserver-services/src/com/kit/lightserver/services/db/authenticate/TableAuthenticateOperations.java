package com.kit.lightserver.services.db.authenticate;

import com.fap.framework.db.InsertQueryResult;
import com.fap.framework.db.KitDataSource;
import com.fap.framework.db.SelectQueryResult;
import com.fap.framework.db.SelectQueryResultAdapterBoolean;
import com.fap.framework.db.SelectQueryResultAdapterString;
import com.fap.framework.db.SelectQuerySingleResult;
import com.fap.framework.db.UpdateQueryResult;
import com.kit.lightserver.domain.types.ConnectionInfoVO;
import com.kit.lightserver.domain.types.InstallationIdAbVO;
import com.kit.lightserver.services.be.authentication.AuthenticateQueryResult;

public final class TableAuthenticateOperations {

    private final KitDataSource dataSource;

    public TableAuthenticateOperations(final KitDataSource dataSource) {
        this.dataSource = dataSource;
    }

    private final SelectClientIdAndPasswordResultAdapter queryResultAdapter = new SelectClientIdAndPasswordResultAdapter();

    private final SelectQueryResultAdapterBoolean selectMustResetResultAdapter = new SelectQueryResultAdapterBoolean();

    private final SelectQueryResultAdapterString selectCelularIdABAdapter = new SelectQueryResultAdapterString();

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

    public UpdateQueryResult updateLastDisconnection(final String userClientId) {
        UpdateAuthenticateLastDisconnectionQuery updateQuery = new UpdateAuthenticateLastDisconnectionQuery(userClientId);
        UpdateQueryResult result = dataSource.executeUpdateQuery(updateQuery);
        return result;
    }

    public SelectQueryResult<SelectQuerySingleResult<String>> selectLastConnection(final String userClientId) {
        SelectAuthenticateUltimaConexaoQuery selectQuery = new SelectAuthenticateUltimaConexaoQuery(userClientId);
        SelectQueryResult<SelectQuerySingleResult<String>> result = dataSource.executeSelectQuery(selectQuery, selectCelularIdABAdapter);
        return result;
    }

    public InsertQueryResult firstInsertLastConnection(final String ktClientId, final InstallationIdAbVO installationIdSTY, final ConnectionInfoVO connectionInfo) {
        InsertAuthenticateUltimaConexaoQuery insertQuery = new InsertAuthenticateUltimaConexaoQuery(ktClientId, installationIdSTY, connectionInfo);
        InsertQueryResult result = dataSource.executeInsertQuery(insertQuery);
        return result;
    }

}// class
