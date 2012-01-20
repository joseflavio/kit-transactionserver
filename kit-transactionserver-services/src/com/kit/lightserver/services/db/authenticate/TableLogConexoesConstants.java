package com.kit.lightserver.services.db.authenticate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kit.lightserver.services.be.authentication.AuthenticationServiceResponse;

public final class TableLogConexoesConstants {

    static public final String TABLE_LOG_CONEXOES = "dbo.LogConexoes";

    static private final Logger LOGGER = LoggerFactory.getLogger(TableLogConexoesConstants.class);

    static public int convertToStatus(final AuthenticationServiceResponse authenticationResponse) {

        final int status;

        switch (authenticationResponse) {

        case SUCCESS_NO_NEED_TO_RESET:
            status = 100;
            break;
        case SUCCESS_MUST_RESET:
            status = 110;
            break;
        case FAILED_CLIENTID_DO_NOT_EXIST:
            status = 200;
            break;
        case FAILED_INVALID_PASSWORD:
            status = 210;
            break;
        case FAILED_USER_ALREADY_LOGGEDIN:
            status = 220;
            break;
        case ERROR:
            status = 300;
            break;
        default:
            LOGGER.error("Impossible to convert. authenticationResponse=" + authenticationResponse);
            status = 999;
            break;

        }// switch

        return status;

    }

}// class
