package com.kit.lightserver.statemachine;

import com.kit.lightserver.services.be.authentication.AuthenticationServiceResponse;

public final class ClientInfoCTX {

    private final String ktClientId;
    private final boolean authenticated;
    private final AuthenticationServiceResponse authenticationResponse;
    private final boolean mustReset;

    public ClientInfoCTX(final String ktClientId, final AuthenticationServiceResponse authenticationResponse, final boolean authenticated,
            final boolean mustReset) {

        this.ktClientId = ktClientId;
        this.authenticationResponse = authenticationResponse;
        this.authenticated = authenticated;
        this.mustReset = mustReset;

    }// constructor

    public String getKtClientId() {
        return ktClientId;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public boolean isMustReset() {
        return mustReset;
    }

    @Override
    public String toString() {
        return "ClientInfoCTX [ktClientId=" + ktClientId + ", authenticated=" + authenticated + ", authenticationResponse=" + authenticationResponse
                + ", mustReset=" + mustReset + "]";
    }

}// class
