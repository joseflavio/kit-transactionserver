package com.kit.lightserver.adapterin;

import kit.primitives.authentication.AuthenticationRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kit.lightserver.domain.AuthenticationRequestTypeEnumSTY;
import com.kit.lightserver.services.types.InstallationIdSTY;
import com.kit.lightserver.statemachine.events.AuthenticationRequestSME;

public final class AdiAuthenticationRequest {

    static private final Logger LOGGER = LoggerFactory.getLogger(AdiAuthenticationRequest.class);

    static public AuthenticationRequestSME adapt(final AuthenticationRequest primitive) {

        final String clientId = primitive.clientId;
        final String password = primitive.password;

        final long installationId1 = primitive.installationId1;
        final long installationId2 = primitive.installationId2;
        final InstallationIdSTY installationIdSTY = new InstallationIdSTY(installationId1, installationId2);


        final byte type = primitive.type;

        final AuthenticationRequestTypeEnumSTY authenticationRequestType;
        if( type == AuthenticationRequest.NEWLOGIN ) {
            authenticationRequestType = AuthenticationRequestTypeEnumSTY.NEWLOGIN;
        }
        else if( type == AuthenticationRequest.PREVIOUS ) {
            authenticationRequestType = AuthenticationRequestTypeEnumSTY.PREVIOUS;
        }
        else {
            LOGGER.error("Unknow type of AuthenticationRequest primitive. type=" + type);
            return null;
        }

        final AuthenticationRequestSME result = new AuthenticationRequestSME(clientId, password, authenticationRequestType, installationIdSTY);
        return result;

    }

}// class
