package com.kit.lightserver.services.be.authentication;

import org.dajo.framework.db.InsertQueryResult;
import org.dajo.framework.db.SelectQueryResult;
import org.dajo.framework.db.SingleConnectionQueryExecutor;
import org.dajo.framework.db.UpdateQueryResult;
import org.dajo.framework.db.resultadapters.SelectQuerySingleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kit.lightserver.domain.types.AuthenticationRequestTypeEnumSTY;
import com.kit.lightserver.domain.types.ConnectionInfoVO;
import com.kit.lightserver.domain.types.InstallationIdAbVO;
import com.kit.lightserver.services.db.authenticate.SelectAuthenticateUltimoSucessoResult;
import com.kit.lightserver.services.db.authenticate.TableAuthenticateOperations;
import com.kit.lightserver.services.db.authenticategpsenabled.TableAuthenticateGpsEnabledOperations;

final class AuthenticationServiceInternal {

    static private final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceInternal.class);

    private final TableAuthenticateOperations authenticateOperations;

    private final TableAuthenticateGpsEnabledOperations authenticateGpsEnabledOperations;

    public AuthenticationServiceInternal(final SingleConnectionQueryExecutor dbaQueryExecutor, final SingleConnectionQueryExecutor dbdQueryExecutor) {
        this.authenticateOperations = new TableAuthenticateOperations(dbaQueryExecutor, dbdQueryExecutor);
        this.authenticateGpsEnabledOperations = new TableAuthenticateGpsEnabledOperations(dbaQueryExecutor);
    }

    AuthenticationServiceResponse authenticate(final ConnectionInfoVO connectionInfo, final String clientUserId, final String password,
            final InstallationIdAbVO installIdAb, final AuthenticationRequestTypeEnumSTY authRequestType) {


        try {

            /*
             * First get the last valid authentication to verify if it is the same client connecting
             */
            AuthenticateLastSuccessfulServiceResponse lastSuccessfulAuthenticationResult =
                    getLastSuccessfulAuthentication(clientUserId, installIdAb, connectionInfo);

            if (AuthenticationRequestTypeEnumSTY.RES_MANUAL == authRequestType || AuthenticationRequestTypeEnumSTY.RES_AUTOMATIC == authRequestType) {
                if (lastSuccessfulAuthenticationResult.getLastInstallationIdAb().equals(installIdAb) == false) {
                    return AuthenticationServiceResponse.getFailedInstance(AuthenticationServiceResponse.FailureType.FAILED_NEWINSTALLATIONID_NO_AUTO_UPDATE);
                }
            }

            /*
             * Checking the password
             */
            AuthenticationServiceResponse checkPasswordResponse = checkPassword(clientUserId, password, authRequestType);

            if ( checkPasswordResponse.isSuccess() == true ) {

                /*
                 * Updating last successful authentication table
                 */
                UpdateQueryResult result = authenticateOperations.updateAuthUltimoSucesso(installIdAb, connectionInfo, clientUserId,
                        lastSuccessfulAuthenticationResult);

                if (result.isUpdateQuerySuccessful() == false) {
                    return AuthenticationServiceResponse.getFailedInstance(AuthenticationServiceResponse.FailureType.FAILED_DATABASE_ERROR);
                }
                if (result.getRowsUpdated() != 1) {
                    return AuthenticationServiceResponse.getFailedInstance(AuthenticationServiceResponse.FailureType.FAILED_SIMULTANEUS_LOGIN);
                }

            }

            return checkPasswordResponse;

        }
        catch (Exception e) {
            LOGGER.error("Unexpected Error.", e); // This shall never happen
            return AuthenticationServiceResponse.getFailedInstance(AuthenticationServiceResponse.FailureType.FAILED_UNEXPECTED_ERROR);
        }
    }

    private AuthenticateLastSuccessfulServiceResponse getLastSuccessfulAuthentication(final String clientUserId, final InstallationIdAbVO installationId,
            final ConnectionInfoVO connectionInfo) {

        /*
         * Updating the time of the Last Successful Authentication
         */
        SelectQueryResult<SelectAuthenticateUltimoSucessoResult> selectLastConnectionQueryResult = authenticateOperations
                .selectLastSuccessAuthentication(clientUserId);

        if (selectLastConnectionQueryResult.isSelectQuerySuccessful() == false) {
            return AuthenticateLastSuccessfulServiceResponse.FAILED_DATABASE_ERROR;
        }

        if (selectLastConnectionQueryResult.getResult().isAvailable() == false) {
            LOGGER.warn("ClientId not found in the table. clientUserId=" + clientUserId);
            InsertQueryResult firstInsertLastConnectionResult = authenticateOperations.firstInsertLastConnection(clientUserId, installationId,
                    connectionInfo);
            if (firstInsertLastConnectionResult.isInsertQuerySuccessfull() == false) {
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

        final SelectQueryResult<AuthenticateQueryResult> resultContainer = authenticateOperations.selectClientIdExists(clientUserId);

        if (resultContainer.isSelectQuerySuccessful() == false) { // Just checking if the query was successful
            return AuthenticationServiceResponse.getFailedInstance(AuthenticationServiceResponse.FailureType.FAILED_DATABASE_ERROR);
        }

        /*
         * Failure cases due to Incorrect Password
         */
        final AuthenticateQueryResult result = resultContainer.getResult();
        final boolean userExists = result.isUserExists();
        if (userExists == false) {
            return AuthenticationServiceResponse.getFailedInstance(AuthenticationServiceResponse.FailureType.FAILED_CLIENTID_DO_NOT_EXIST);
        }

        if (result.getPassword().equals(password) == false) {
            return AuthenticationServiceResponse.getFailedInstance(AuthenticationServiceResponse.FailureType.FAILED_INVALID_PASSWORD);
        }

        /*
         * Must reset
         */
        SelectQueryResult<SelectQuerySingleResult<Boolean>> mustResetResultContainer = authenticateOperations.selectMustReset(clientUserId);
        if (mustResetResultContainer.isSelectQuerySuccessful() == false) {
            return AuthenticationServiceResponse.getFailedInstance(AuthenticationServiceResponse.FailureType.FAILED_DATABASE_ERROR);
        }

        boolean deveResetar = true;
        if ( mustResetResultContainer.getResult().isAvailable() == true ) {
            Boolean deveResetarBoolean = mustResetResultContainer.getResult().getValue();
            deveResetar = deveResetarBoolean.booleanValue();
        }
        else {
            LOGGER.warn("UserClientId not found in the table [AuthenticateDeveResetar]. clientUserId={}", clientUserId);
            InsertQueryResult insertResult = authenticateOperations.firstInsertMustReset(clientUserId);
            if ( insertResult.isInsertQuerySuccessfull() == false ) {
                return AuthenticationServiceResponse.getFailedInstance(AuthenticationServiceResponse.FailureType.FAILED_DATABASE_ERROR);
            }
            deveResetar = false;
        }

        /*
         * GPS Enabled
         */
        SelectQueryResult<SelectQuerySingleResult<Boolean>> gpsEnabledQueryResult = authenticateGpsEnabledOperations.selectGpsEnabled(clientUserId);
        if( gpsEnabledQueryResult.isSelectQuerySuccessful() == false ) {
            return AuthenticationServiceResponse.getFailedInstance(AuthenticationServiceResponse.FailureType.FAILED_DATABASE_ERROR);
        }

        final boolean gpsEnabled;
        if( gpsEnabledQueryResult.getResult().isAvailable() == true ) {
            gpsEnabled = gpsEnabledQueryResult.getResult().getValue().booleanValue();
        }
        else {
            LOGGER.warn("UserClientId not found in the table [AuthenticateGpsEnabled]. clientUserId={}", clientUserId);
            InsertQueryResult insertResult = authenticateGpsEnabledOperations.firstInsertGpsEnabled(clientUserId);
            if( insertResult.isInsertQuerySuccessfull() == false ) {
                return AuthenticationServiceResponse.getFailedInstance(AuthenticationServiceResponse.FailureType.FAILED_DATABASE_ERROR);
            }
            gpsEnabled = false;
        }


        /*
         * Success case
         */
        if (AuthenticationRequestTypeEnumSTY.RES_MANUAL_NEW_USER == authRequestType) {
            deveResetar = true;
        }

        return AuthenticationServiceResponse.getSuccessInstance(deveResetar, gpsEnabled);

    }

}// class
