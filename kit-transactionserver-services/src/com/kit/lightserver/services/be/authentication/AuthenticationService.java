package com.kit.lightserver.services.be.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fap.framework.db.DatabaseConfig;
import com.fap.framework.db.InsertQueryResult;
import com.fap.framework.db.KitDataSource;
import com.fap.framework.db.KitDataSourceSimple;
import com.fap.framework.db.SelectQueryResult;
import com.fap.framework.db.SelectQuerySingleResult;
import com.fap.framework.db.UpdateQueryResult;
import com.fap.thread.RichThreadFactory;
import com.jfap.framework.configuration.ConfigAccessor;
import com.kit.lightserver.domain.types.AuthenticationRequestTypeEnumSTY;
import com.kit.lightserver.domain.types.ConnectionInfoVO;
import com.kit.lightserver.domain.types.InstallationIdAbVO;
import com.kit.lightserver.services.db.authenticate.SelectAuthenticateLastSuccessResult;
import com.kit.lightserver.services.db.authenticate.TableAuthenticateOperations;
import com.kit.lightserver.services.db.logconnection.LogConectionTask;

public final class AuthenticationService {

    static private final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    static public AuthenticationService getInstance(final ConfigAccessor configAccessor) {
        DatabaseConfig dbConfig = DatabaseConfig.getInstance(configAccessor);
        KitDataSource dataSource = new KitDataSourceSimple(dbConfig);
        return new AuthenticationService(dataSource);
    }

    private final KitDataSource dataSource;

    private final TableAuthenticateOperations tableAuthenticateOperations;

    private AuthenticationService(final KitDataSource dataSource) {
        this.dataSource = dataSource;
        this.tableAuthenticateOperations = new TableAuthenticateOperations(dataSource);
    }

    public AuthenticationServiceResponse authenticate(final ConnectionInfoVO connectionInfo, final String userClientId, final String password,
            final InstallationIdAbVO installIdAb, final AuthenticationRequestTypeEnumSTY authRequestType) {

        AuthenticationServiceResponse authenticationResponse = this.authenticate2(connectionInfo, userClientId, password, installIdAb, authRequestType);

        Integer status = AuthenticationServiceStatusConstants.convertToStatus(authenticationResponse);
        LogConectionTask logConectionTask = new LogConectionTask(dataSource, connectionInfo, installIdAb, userClientId, status);

        RichThreadFactory t3Factory = new RichThreadFactory("T3", connectionInfo);
        Thread logConectionThread = t3Factory.newThread(logConectionTask);
        logConectionThread.start();

        return authenticationResponse;

    }

    public AuthenticationServiceResponse authenticate2(final ConnectionInfoVO connectionInfo, final String userClientId, final String password,
            final InstallationIdAbVO installIdAb, final AuthenticationRequestTypeEnumSTY authRequestType) {

        try {

            AuthenticateLastSuccessfulServiceResponse lastSuccessfulAuthenticationResult =
                    this.getLastSuccessfulAuthentication(userClientId, installIdAb, connectionInfo);

            if( AuthenticationRequestTypeEnumSTY.RES_MANUAL == authRequestType || AuthenticationRequestTypeEnumSTY.RES_AUTOMATIC == authRequestType ) {
                if( lastSuccessfulAuthenticationResult.getLastInstallationIdAb().equals(installIdAb) == false ) {
                    return AuthenticationServiceResponse.FAILED_NEWINSTALLATIONID_NO_AUTO_UPDATE;
                }
            }

            /*
             * Checking the password
             */
            AuthenticationServiceResponse checkPasswordResponse = this.checkPassword(userClientId, password, authRequestType);

            if( checkPasswordResponse == AuthenticationServiceResponse.SUCCESS_MUST_RESET ||
                checkPasswordResponse == AuthenticationServiceResponse.SUCCESS_NO_NEED_TO_RESET ) {

                /*
                 * Updating last successful authentication table
                 */
                UpdateQueryResult result = tableAuthenticateOperations.updateAuthUltimoSucesso(
                        installIdAb,  connectionInfo, userClientId, lastSuccessfulAuthenticationResult);

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
            final String userClientId, final InstallationIdAbVO installationId, final ConnectionInfoVO connectionInfo) {

        /*
         * Updating the time of the Last Successful Authentication
         */
        SelectQueryResult<SelectAuthenticateLastSuccessResult> selectLastConnectionQueryResult =
                tableAuthenticateOperations.selectLastSuccessAuthentication(userClientId);

        if( selectLastConnectionQueryResult.isSelectQuerySuccessful() == false ) {
            return AuthenticateLastSuccessfulServiceResponse.FAILED_DATABASE_ERROR;
        }

        if( selectLastConnectionQueryResult.getResult().isAvailable() == false ) {
            LOGGER.warn("ClientId not found in the table. userClientId="+userClientId);
            InsertQueryResult firstInsertLastConnectionResult = tableAuthenticateOperations.firstInsertLastConnection(userClientId, installationId, connectionInfo);
            if( firstInsertLastConnectionResult.isQuerySuccessfullyExecuted() == false ) {
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

    private AuthenticationServiceResponse checkPassword(final String userClientId, final String password, final AuthenticationRequestTypeEnumSTY authRequestType) {

        final SelectQueryResult<AuthenticateQueryResult> resultContainer = tableAuthenticateOperations.selectClientIdExists(userClientId);

        if( resultContainer.isSelectQuerySuccessful() == false ) { // Just checking if the query was successful
            return AuthenticationServiceResponse.FAILED_DATABASE_ERROR;
        }

        /*
         * Failure cases due to Incorrect Password
         */
        final AuthenticateQueryResult result = resultContainer.getResult();
        final boolean userExists = result.isUserExists().booleanValue();
        if(  userExists == false ) {
            return AuthenticationServiceResponse.FAILED_CLIENTID_DO_NOT_EXIST;
        }

        if( result.getKtPassword().equals(password) == false ) {
            return AuthenticationServiceResponse.FAILED_INVALID_PASSWORD;
        }

        /*
         * Decide if must reset
         */
        SelectQueryResult<SelectQuerySingleResult<Boolean>> mustResetResultContainer = tableAuthenticateOperations.selectMustReset(userClientId);
        if( mustResetResultContainer.isSelectQuerySuccessful() == false ) {
            return AuthenticationServiceResponse.FAILED_DATABASE_ERROR;
        }

        final boolean deveResetar;
        if( mustResetResultContainer.getResult().isAvailable() == false ) {
            LOGGER.warn("ClientId not found in the AuthenticateDeveResetar table. userClientId="+userClientId);
            InsertQueryResult firstInsertMustResetResult = tableAuthenticateOperations.firstInsertMustReset(userClientId);
            if(firstInsertMustResetResult.isQuerySuccessfullyExecuted() == false) {
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

    public boolean logOff(final String userClientId, final boolean mustResetInNextConnection) {

        UpdateQueryResult updateMustResetResult = tableAuthenticateOperations.updateMustReset(userClientId, mustResetInNextConnection);
        if (updateMustResetResult.isUpdateQuerySuccessful() == false) {
            return false;
        }
        if (updateMustResetResult.getRowsUpdated() != 1) {
            return false;
        }

        UpdateQueryResult updateLastDisconnectionResult = tableAuthenticateOperations.updateLastDisconnection(userClientId);
        if (updateLastDisconnectionResult.isUpdateQuerySuccessful() == false) {
            return false;
        }
        if (updateLastDisconnectionResult.getRowsUpdated() != 1) {
            return false;
        }

        return true;

    }

}// class
