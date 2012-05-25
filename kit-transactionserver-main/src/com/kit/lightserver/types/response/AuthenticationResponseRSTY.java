package com.kit.lightserver.types.response;

import com.kit.lightserver.services.be.authentication.AuthenticationServiceResponse;


public final class AuthenticationResponseRSTY implements ClientResponseRSTY {

    private final AuthenticationServiceResponse type;

    public AuthenticationResponseRSTY(final AuthenticationServiceResponse type) {
        this.type = type;
    }

    public AuthenticationServiceResponse getType() {
        return type;
    }

    @Override
    public String toString() {
        return "AuthenticationResponseFailedWrongPassowordRSTY [type=" + type + "]";
    }


}// class
