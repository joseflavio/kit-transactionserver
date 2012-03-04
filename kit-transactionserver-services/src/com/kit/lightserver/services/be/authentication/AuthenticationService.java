package com.kit.lightserver.services.be.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fap.framework.db.DataSource;
import com.fap.framework.db.DatabaseConfig;
import com.fap.framework.db.InsertQueryResult;
import com.fap.framework.db.SelectQueryResult;
import com.fap.framework.db.UpdateQueryResult;
import com.jfap.framework.configuration.ConfigAccessor;
import com.kit.lightserver.domain.types.ConnectionInfo;
import com.kit.lightserver.domain.types.InstallationIdSTY;
import com.kit.lightserver.services.db.authenticate.TableAuthenticateOperations;
import com.kit.lightserver.services.db.authenticate.TableLogConexoesConstants;
import com.kit.lightserver.services.db.authenticate.TableLogConexoesOperations;

public final class AuthenticationService {

    static private final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    static public AuthenticationService getInstance(final ConfigAccessor configAccessor) {
        DatabaseConfig dbConfig = DatabaseConfig.getInstance(configAccessor);
        DataSource dataSource = new DataSource(dbConfig);
        return new AuthenticationService(dbConfig);
    }

    private final TableLogConexoesOperations logConexoesOperations;

    private final TableAuthenticateOperations tableAuthenticateOperations;

    private AuthenticationService(final DatabaseConfig dbConfig) {
        this.logConexoesOperations = new TableLogConexoesOperations(dbConfig);
        this.tableAuthenticateOperations = new TableAuthenticateOperations(dbConfig);
    }

    public AuthenticationServiceResponse authenticate(final ConnectionInfo connectionId, final String userClientId, final String password,
            final InstallationIdSTY installationId, final long lastConnectionToken) {

        final AuthenticationServiceResponse authenticationResponse = this.checkAuthentication(userClientId, password);

        final int status = TableLogConexoesConstants.convertToStatus(authenticationResponse);

        final InsertQueryResult registerConnectionResult = logConexoesOperations.registerConnection(connectionId, installationId, userClientId, status);

        if (registerConnectionResult.isQuerySuccessfullyExecuted() == false) {
            LOGGER.error("Unexpected error when loggin the connection. ktConexaoId=" + connectionId + ", registerConnectionResult=" + registerConnectionResult);
        }

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
         * Success
         * ...but it still needs to update the last authentication
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
