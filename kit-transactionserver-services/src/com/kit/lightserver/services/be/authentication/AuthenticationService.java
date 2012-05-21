package com.kit.lightserver.services.be.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fap.framework.db.DatabaseConfig;
import com.fap.framework.db.KitDataSource;
import com.fap.framework.db.KitDataSourceSimple;
import com.fap.framework.db.SelectQueryResult;
import com.fap.framework.db.SelectQueryResultSingleBoolean;
import com.fap.framework.db.UpdateQueryResult;
import com.fap.thread.RichThreadFactory;
import com.jfap.framework.configuration.ConfigAccessor;
import com.kit.lightserver.domain.types.ConnectionInfoVO;
import com.kit.lightserver.domain.types.InstallationIdSTY;
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

    public AuthenticationServiceResponse authenticate(final ConnectionInfoVO connectionId, final String userClientId, final String password,
            final InstallationIdSTY installationId, final long lastConnectionToken) {

        try {

            AuthenticationServiceResponse authenticationResponse = this.checkAuthentication(userClientId, password);
            Integer status = AuthenticationServiceStatusConstants.convertToStatus(authenticationResponse);

            LogConectionTask logConectionTask = new LogConectionTask(dataSource, connectionId, installationId, userClientId, status);

            RichThreadFactory t3Factory = new RichThreadFactory("T3", connectionId);
            Thread logConectionThread = t3Factory.newThread(logConectionTask);
            logConectionThread.start();

            return authenticationResponse;

        } catch (Exception e) {
            LOGGER.error("Unexpected Error (Should never occur)", e);
            return AuthenticationServiceResponse.FAILED_UNEXPECTED_ERROR;
        }

    }

    private AuthenticationServiceResponse checkAuthentication(final String userClientId, final String password) {

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
        SelectQueryResult<SelectQueryResultSingleBoolean> mustResetResultContainer = tableAuthenticateOperations.selectMustReset(userClientId);
        if( mustResetResultContainer.isSelectQuerySuccessful() == false ) {
            return AuthenticationServiceResponse.FAILED_DATABASE_ERROR;
        }

        /*
         * Updating the time of the Last Successful Authentication
         */
        final UpdateQueryResult updateLastAuthenticationResultContainer = tableAuthenticateOperations.updateClientLoggedIn(userClientId);
        if( updateLastAuthenticationResultContainer.isUpdateQuerySuccessful() == false ) {
            return AuthenticationServiceResponse.FAILED_DATABASE_ERROR;
        }

        /*
         * Success cases
         */
        final boolean deveResetar;
        if( mustResetResultContainer.getResult().isAvailable() == false ) {
            LOGGER.warn("ClientId not found in the AuthenticateDeveResetar table. userClientId="+userClientId);
            deveResetar = false;
        }
        else {
            deveResetar = mustResetResultContainer.getResult().getValue();
        }

        if( deveResetar == true ) {
            return AuthenticationServiceResponse.SUCCESS_MUST_RESET;

        }
        else {
            return AuthenticationServiceResponse.SUCCESS_NO_NEED_TO_RESET;
        }

    }

    public boolean logOff(final String ktClientId, final boolean mustResetInNextConnection) {

        final UpdateQueryResult result = tableAuthenticateOperations.updateClientLoggedOff(ktClientId, mustResetInNextConnection);
        if (result.isUpdateQuerySuccessful() == false) {
            return false;
        }

        if (result.getRowsUpdated() != 1) {
            return false;
        }

        return true;

    }

}// class
