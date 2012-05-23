package com.kit.lightserver.adapters.adapterin;

import kit.primitives.authentication.AuthenticationRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kit.lightserver.domain.AuthenticationRequestTypeEnumSTY;
import com.kit.lightserver.domain.types.InstallationIdAbVO;
import com.kit.lightserver.statemachine.events.AuthenticationRequestSME;

public final class AdiAuthenticationRequest {

    static private final Logger LOGGER = LoggerFactory.getLogger(AdiAuthenticationRequest.class);

    static public AuthenticationRequestSME adapt(final AuthenticationRequest primitive) {

        final String clientId = primitive.clientId;
        final String password = primitive.password;

        final long installationId1 = primitive.installationId1;
        final long installationId2 = primitive.installationId2;
        final InstallationIdAbVO installationIdSTY = new InstallationIdAbVO(installationId1, installationId2);

        final byte type = primitive.type;

        final AuthenticationRequestTypeEnumSTY authenticationRequestType;
        if( type == AuthenticationRequest.RES_MANUAL ) {
            authenticationRequestType = AuthenticationRequestTypeEnumSTY.RES_MANUAL;
        }
        else if( type == AuthenticationRequest.RES_MANUAL_NEW_USER ) {
            authenticationRequestType = AuthenticationRequestTypeEnumSTY.RES_MANUAL_NEW_USER;
        }
        else if( type == AuthenticationRequest.RES_AUTOMATIC ) {
            authenticationRequestType = AuthenticationRequestTypeEnumSTY.RES_AUTOMATIC;
        }
        else {
            LOGGER.error("Unknow type of AuthenticationRequest primitive. type=" + type);
            return null;
        }

        final AuthenticationRequestSME result = new AuthenticationRequestSME(clientId, password, authenticationRequestType, installationIdSTY);
        return result;

    }

}// class
