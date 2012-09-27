package com.kit.lightserver.services.be.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kit.lightserver.services.be.authentication.AuthenticationServiceResponse.FailureType;

final class AuthenticationServiceStatusConverter {

    static private final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceStatusConverter.class);

    static public int convertToStatus(final AuthenticationServiceResponse authenticationResponse) {

        final int status;

        if( authenticationResponse.isSuccess() == true ) {
            if( authenticationResponse.isMustReset() ) {
                status = 100;
            }
            else {
                status = 110;
            }
        }
        else {
            FailureType failureType = authenticationResponse.getFailureType();
            switch (failureType) {
                case FAILED_CLIENTID_DO_NOT_EXIST:
                    status = 200;
                    break;
                case FAILED_INVALID_PASSWORD:
                    status = 210;
                    break;
                case FAILED_DATABASE_ERROR:
                    status = 220;
                    break;
                case FAILED_SIMULTANEUS_LOGIN:
                    status = 230;
                    break;
                case FAILED_NEWINSTALLATIONID_NO_AUTO_UPDATE:
                    status = 240;
                    break;
                case FAILED_UNEXPECTED_ERROR:
                    status = 666;
                    break;
                default:
                    LOGGER.error("Impossible to convert. authenticationResponse=" + authenticationResponse);
                    status = 999;
                    break;
            }
        }

        return status;

    }

}// class
