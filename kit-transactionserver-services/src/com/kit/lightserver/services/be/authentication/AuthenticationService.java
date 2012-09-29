package com.kit.lightserver.services.be.authentication;

import org.dajo.framework.configuration.ConfigAccessor;
import org.dajo.framework.db.DatabaseConfig;
import org.dajo.framework.db.SimpleQueryExecutor;
import org.dajo.framework.db.SingleConnectionQueryExecutor;
import org.dajo.framework.db.UpdateQueryResult;

import com.fap.thread.RichThreadFactory;

import com.kit.lightserver.domain.types.AuthenticationRequestTypeEnumSTY;
import com.kit.lightserver.domain.types.ConnectionInfoVO;
import com.kit.lightserver.domain.types.InstallationIdAbVO;
import com.kit.lightserver.services.be.common.DatabaseAliases;
import com.kit.lightserver.services.db.authenticate.OperationAuthenticateDeveResetar;
import com.kit.lightserver.services.db.dbl.LogConexoesFinalizadasTask;
import com.kit.lightserver.services.db.dbl.LogConexoesIniciadasTask;

public final class AuthenticationService {

    //static private final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    static public AuthenticationService getInstance(final ConfigAccessor configAccessor) {
        return new AuthenticationService(configAccessor);
    }

    private final DatabaseConfig dbaConfig;
    private final DatabaseConfig dbdConfig;
    private final ConfigAccessor configAccessor;

    private AuthenticationService(final ConfigAccessor configAccessor) {
        this.configAccessor = configAccessor;
        this.dbaConfig = DatabaseConfig.getInstance(configAccessor, DatabaseAliases.DBA, true);
        this.dbdConfig = DatabaseConfig.getInstance(configAccessor, DatabaseAliases.DBD, true);
    }

    public AuthenticationServiceResponse authenticate(final ConnectionInfoVO connectionInfo, final String clientUserId, final String password,
            final InstallationIdAbVO installIdAb, final AuthenticationRequestTypeEnumSTY authRequestType) {

        SingleConnectionQueryExecutor dbaQueryExecutor = new SingleConnectionQueryExecutor(dbaConfig);
        SingleConnectionQueryExecutor dbdQueryExecutor = new SingleConnectionQueryExecutor(dbdConfig);

        AuthenticationServiceInternal serviceInternal = new AuthenticationServiceInternal(dbaQueryExecutor, dbdQueryExecutor);

        AuthenticationServiceResponse authenticationResponse = serviceInternal.authenticate(connectionInfo, clientUserId, password, installIdAb, authRequestType);

        dbaQueryExecutor.finish();
        dbdQueryExecutor.finish();

        int responseStatusForLog = AuthenticationServiceStatusConverter.convertToStatus(authenticationResponse);
        LogConexoesIniciadasTask logConectionTask = new LogConexoesIniciadasTask(configAccessor, connectionInfo, installIdAb, clientUserId, responseStatusForLog);
        Thread logConectionThread = RichThreadFactory.newThread(logConectionTask, connectionInfo);
        logConectionThread.start();

        return authenticationResponse;

    }

    public boolean logOff(final String clientUserId, final boolean mustResetInNextConnection, final ConnectionInfoVO connectionInfo) {

        SimpleQueryExecutor dbdQueryExecutor = new SimpleQueryExecutor(dbdConfig);
        UpdateQueryResult updateMustResetResult = OperationAuthenticateDeveResetar.updateMustReset(dbdQueryExecutor, clientUserId, mustResetInNextConnection);

        final boolean successLogOff;
        if (updateMustResetResult.isUpdateQuerySuccessful() == false) {
            successLogOff = false;
        }
        else if (updateMustResetResult.getRowsUpdated() != 1) {
            successLogOff = false;
        }
        else {
            successLogOff = true;
        }

        LogConexoesFinalizadasTask logConectionTask = new LogConexoesFinalizadasTask(configAccessor, clientUserId, connectionInfo);
        Thread logConectionThread = RichThreadFactory.newThread(logConectionTask, connectionInfo);
        logConectionThread.start();

        return successLogOff;

    }

}// class
