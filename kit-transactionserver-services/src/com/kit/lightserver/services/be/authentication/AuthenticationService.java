package com.kit.lightserver.services.be.authentication;

import com.fap.framework.db.DatabaseConfig;
import com.fap.framework.db.KitDataSource;
import com.fap.framework.db.KitDataSourceSimple;
import com.fap.framework.db.SelectQueryResult;
import com.fap.framework.db.UpdateQueryResult;
import com.fap.thread.RichThreadFactory;
import com.jfap.framework.configuration.ConfigAccessor;
import com.kit.lightserver.domain.types.ConnectionInfoVO;
import com.kit.lightserver.domain.types.InstallationIdSTY;
import com.kit.lightserver.services.db.authenticate.TableAuthenticateOperations;
import com.kit.lightserver.services.db.authenticate.TableLogConexoesConstants;
import com.kit.lightserver.services.db.logconnection.LogConectionTask;

public final class AuthenticationService {

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

        AuthenticationServiceResponse authenticationResponse = this.checkAuthentication(userClientId, password);
        int status = TableLogConexoesConstants.convertToStatus(authenticationResponse);

        LogConectionTask logConectionTask = new LogConectionTask(dataSource, connectionId, installationId, userClientId, status);
        RichThreadFactory t3Factory = new RichThreadFactory("T3", connectionId);
        Thread logConectionThread = t3Factory.newThread(logConectionTask);
        logConectionThread.start();

        return authenticationResponse;

    }

    private AuthenticationServiceResponse checkAuthentication(final String userClientId, final String password) {

        final SelectQueryResult<AuthenticateQueryResult> resultContainer = tableAuthenticateOperations.selectClientIdExists(userClientId);

        if( resultContainer.isQuerySuccessful() == false ) { // Just checking if the query was successful
            return AuthenticationServiceResponse.ERROR;
        }

        /*
         * Failed Cases
         */
        final AuthenticateQueryResult result = resultContainer.getResult();
        final boolean userExists = result.isUserExists().booleanValue();
        if(  userExists == false ) {
            return AuthenticationServiceResponse.FAILED_CLIENTID_DO_NOT_EXIST;
        }

        if( result.getKtPassword().equals(password) == false ) {
            return AuthenticationServiceResponse.FAILED_INVALID_PASSWORD;
        }

//        if( result.isKtUsuarioConectado() == true ) {
//            return AuthenticationServiceResponse.FAILED_USER_ALREADY_LOGGEDIN;
//        }

        /*
         * Success ...but it still needs to update the last authentication.
         */
        final UpdateQueryResult updateLastAuthenticationResultContainer = tableAuthenticateOperations.updateClientLoggedIn(userClientId);
        if( updateLastAuthenticationResultContainer.isUpdateQuerySuccessful() == false ) {
            return AuthenticationServiceResponse.ERROR;
        }

        final boolean deveResetar = result.isKtDeveResetar().booleanValue();
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
