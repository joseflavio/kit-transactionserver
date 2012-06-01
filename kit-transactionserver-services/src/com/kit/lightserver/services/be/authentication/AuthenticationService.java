package com.kit.lightserver.services.be.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fap.framework.db.DatabaseConfig;
import com.fap.framework.db.InsertQueryResult;
import com.fap.framework.db.QueryExecutor;
import com.fap.framework.db.SimpleQueryExecutor;
import com.fap.framework.db.SelectQueryResult;
import com.fap.framework.db.SelectQuerySingleResult;
import com.fap.framework.db.UpdateQueryResult;
import com.fap.thread.RichThreadFactory;
import com.jfap.framework.configuration.ConfigAccessor;
import com.kit.lightserver.domain.types.AuthenticationRequestTypeEnumSTY;
import com.kit.lightserver.domain.types.ConnectionInfoVO;
import com.kit.lightserver.domain.types.InstallationIdAbVO;
import com.kit.lightserver.services.db.authenticate.SelectAuthenticateUltimoSucessoResult;
import com.kit.lightserver.services.db.authenticate.TableAuthenticateOperations;
import com.kit.lightserver.services.db.log.LogConexoesFinalizadasTask;
import com.kit.lightserver.services.db.log.LogConexoesIniciadasTask;

public final class AuthenticationService {

    static private final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    static public AuthenticationService getInstance(final ConfigAccessor configAccessor) {
        DatabaseConfig dbConfig = DatabaseConfig.getInstance(configAccessor);
        QueryExecutor dataSource = new SimpleQueryExecutor(dbConfig);
        return new AuthenticationService(dataSource);
    }

    private final QueryExecutor dataSource;

    private final TableAuthenticateOperations tableAuthenticateOperations;

    private AuthenticationService(final QueryExecutor dataSource) {
        this.dataSource = dataSource;
        this.tableAuthenticateOperations = new TableAuthenticateOperations(dataSource);
    }

    public AuthenticationServiceResponse authenticate(final ConnectionInfoVO connectionInfo, final String clientUserId, final String password,
            final InstallationIdAbVO installIdAb, final AuthenticationRequestTypeEnumSTY authRequestType) {

        AuthenticationServiceResponse authenticationResponse = this.authenticate2(connectionInfo, clientUserId, password, installIdAb, authRequestType);

        int responseStatusForLog = AuthenticationServiceStatusConverter.convertToStatus(authenticationResponse);
        LogConexoesIniciadasTask logConectionTask = new LogConexoesIniciadasTask(dataSource, connectionInfo, installIdAb, clientUserId, responseStatusForLog);

        Thread logConectionThread = RichThreadFactory.newThread(logConectionTask, connectionInfo);
        logConectionThread.start();

        return authenticationResponse;

    }

    private AuthenticationServiceResponse authenticate2(final ConnectionInfoVO connectionInfo, final String clientUserId, final String password,
            final InstallationIdAbVO installIdAb, final AuthenticationRequestTypeEnumSTY authRequestType) {

        try {

            AuthenticateLastSuccessfulServiceResponse lastSuccessfulAuthenticationResult =
                    this.getLastSuccessfulAuthentication(clientUserId, installIdAb, connectionInfo);

            if( AuthenticationRequestTypeEnumSTY.RES_MANUAL == authRequestType || AuthenticationRequestTypeEnumSTY.RES_AUTOMATIC == authRequestType ) {
                if( lastSuccessfulAuthenticationResult.getLastInstallationIdAb().equals(installIdAb) == false ) {
                    return AuthenticationServiceResponse.FAILED_NEWINSTALLATIONID_NO_AUTO_UPDATE;
                }
            }

            /*
             * Checking the password
             */
            AuthenticationServiceResponse checkPasswordResponse = this.checkPassword(clientUserId, password, authRequestType);

            if( checkPasswordResponse == AuthenticationServiceResponse.SUCCESS_MUST_RESET ||
                checkPasswordResponse == AuthenticationServiceResponse.SUCCESS_NO_NEED_TO_RESET ) {

                /*
                 * Updating last successful authentication table
                 */
                UpdateQueryResult result = tableAuthenticateOperations.updateAuthUltimoSucesso(
                        installIdAb,  connectionInfo, clientUserId, lastSuccessfulAuthenticationResult);

                if( result.isUpdateQuerySuccessful() == false ) {
                    return AuthenticationServiceResponse.FAILED_DATABASE_ERROR;
                }
                if( result.getRowsUpdated() != 1 ) {
                    return AuthenticationServiceResponse.FAILED_SIMULTANEUS_LOGIN;
                }

            }

            return checkPasswordResponse;

        } catch (Exception e) {
            LOGGER.error("Unexpected Error (Should never occur)", e);
            return AuthenticationServiceResponse.FAILED_UNEXPECTED_ERROR;
        }

    }

    private AuthenticateLastSuccessfulServiceResponse getLastSuccessfulAuthentication(
            final String clientUserId, final InstallationIdAbVO installationId, final ConnectionInfoVO connectionInfo) {

        /*
         * Updating the time of the Last Successful Authentication
         */
        SelectQueryResult<SelectAuthenticateUltimoSucessoResult> selectLastConnectionQueryResult =
                tableAuthenticateOperations.selectLastSuccessAuthentication(clientUserId);

        if( selectLastConnectionQueryResult.isSelectQuerySuccessful() == false ) {
            return AuthenticateLastSuccessfulServiceResponse.FAILED_DATABASE_ERROR;
        }

        if( selectLastConnectionQueryResult.getResult().isAvailable() == false ) {
            LOGGER.warn("ClientId not found in the table. clientUserId="+clientUserId);
            InsertQueryResult firstInsertLastConnectionResult = tableAuthenticateOperations.firstInsertLastConnection(clientUserId, installationId, connectionInfo);
            if( firstInsertLastConnectionResult.isInsertQuerySuccessfull() == false ) {
                return AuthenticateLastSuccessfulServiceResponse.FAILED_DATABASE_ERROR;
            }
            else {
                return new AuthenticateLastSuccessfulServiceResponse(installationId, connectionInfo.getConnectionUniqueId(), 1);
            }
        }

        String lastInstallationIdAbStr = selectLastConnectionQueryResult.getResult().getLastInstallationIdAb();
        InstallationIdAbVO lastInstallationIdAb = new InstallationIdAbVO(lastInstallationIdAbStr);
        String lastConnectionUniqueId = selectLastConnectionQueryResult.getResult().getLastConnectionUniqueId();
        int lastVersion = selectLastConnectionQueryResult.getResult().getLastVersion();

        return new AuthenticateLastSuccessfulServiceResponse(lastInstallationIdAb, lastConnectionUniqueId, lastVersion);

    }

    private AuthenticationServiceResponse checkPassword(final String clientUserId, final String password, final AuthenticationRequestTypeEnumSTY authRequestType) {

        final SelectQueryResult<AuthenticateQueryResult> resultContainer = tableAuthenticateOperations.selectClientIdExists(clientUserId);

        if( resultContainer.isSelectQuerySuccessful() == false ) { // Just checking if the query was successful
            return AuthenticationServiceResponse.FAILED_DATABASE_ERROR;
        }

        /*
         * Failure cases due to Incorrect Password
         */
        final AuthenticateQueryResult result = resultContainer.getResult();
        final boolean userExists = result.isUserExists();
        if(  userExists == false ) {
            return AuthenticationServiceResponse.FAILED_CLIENTID_DO_NOT_EXIST;
        }

        if( result.getPassword().equals(password) == false ) {
            return AuthenticationServiceResponse.FAILED_INVALID_PASSWORD;
        }

        /*
         * Decide if must reset
         */
        SelectQueryResult<SelectQuerySingleResult<Boolean>> mustResetResultContainer = tableAuthenticateOperations.selectMustReset(clientUserId);
        if( mustResetResultContainer.isSelectQuerySuccessful() == false ) {
            return AuthenticationServiceResponse.FAILED_DATABASE_ERROR;
        }

        final boolean deveResetar;
        if( mustResetResultContainer.getResult().isAvailable() == false ) {
            LOGGER.warn("ClientId not found in the AuthenticateDeveResetar table. clientUserId="+clientUserId);
            InsertQueryResult firstInsertMustResetResult = tableAuthenticateOperations.firstInsertMustReset(clientUserId);
            if(firstInsertMustResetResult.isInsertQuerySuccessfull() == false) {
                return AuthenticationServiceResponse.FAILED_DATABASE_ERROR;
            }
            deveResetar = false;
        }
        else {
            Boolean deveResetarBoolean = mustResetResultContainer.getResult().getValue();
            deveResetar = deveResetarBoolean.booleanValue();
        }

        /*
         * Success cases
         */
        if( AuthenticationRequestTypeEnumSTY.RES_MANUAL_NEW_USER == authRequestType ) {
            return AuthenticationServiceResponse.SUCCESS_MUST_RESET;
        }

        if( deveResetar == true ) {
            return AuthenticationServiceResponse.SUCCESS_MUST_RESET;
        }

        return AuthenticationServiceResponse.SUCCESS_NO_NEED_TO_RESET;

    }

    public boolean logOff(final String clientUserId, final boolean mustResetInNextConnection, final ConnectionInfoVO connectionInfo) {

        UpdateQueryResult updateMustResetResult = tableAuthenticateOperations.updateMustReset(clientUserId, mustResetInNextConnection);

        final boolean successLogOff;
        if (updateMustResetResult.isUpdateQuerySuccessful() == false) {
            successLogOff = false;
        }
        else
        if (updateMustResetResult.getRowsUpdated() != 1) {
            successLogOff = false;
        }
        else {
            successLogOff = true;
        }

        LogConexoesFinalizadasTask logConectionTask = new LogConexoesFinalizadasTask(dataSource, clientUserId);
        Thread logConectionThread = RichThreadFactory.newThread(logConectionTask, connectionInfo);
        logConectionThread.start();

        return successLogOff;

    }

}// class
