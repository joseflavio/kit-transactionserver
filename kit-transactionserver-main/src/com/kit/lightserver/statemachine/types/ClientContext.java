package com.kit.lightserver.statemachine.types;

import com.kit.lightserver.domain.types.InstallationIdAbVO;
import com.kit.lightserver.services.be.authentication.AuthenticationServiceResponse;

public final class ClientContext {

    private final String clientUserId;
    private final InstallationIdAbVO clientInstallId;
    private final boolean authenticated;
    private final AuthenticationServiceResponse authenticationResponse;
    private final Boolean mustReset;
    private final Boolean gpsEnabled;

    public ClientContext(final String clientUserId,  final InstallationIdAbVO clientInstallId, final AuthenticationServiceResponse authenticationResponse, final boolean authenticated,
            final boolean mustReset, final boolean gpsEnabled) {
        this.clientUserId = clientUserId;
        this.clientInstallId = clientInstallId;
        this.authenticationResponse = authenticationResponse;
        this.authenticated = authenticated;
        this.mustReset = Boolean.valueOf(mustReset);
        this.gpsEnabled = Boolean.valueOf(gpsEnabled);
    }

    public ClientContext(final String clientUserId, final InstallationIdAbVO clientInstallId, final AuthenticationServiceResponse authenticationResponse) {
        this.clientUserId = clientUserId;
        this.clientInstallId = clientInstallId;
        this.authenticationResponse = authenticationResponse;
        this.authenticated = false;
        this.mustReset = null;
        this.gpsEnabled = null;
    }

    public String getKtClientUserId() {
        return clientUserId;
    }

    public InstallationIdAbVO getClientInstallId() {
        return clientInstallId;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public boolean isMustReset() {
        return mustReset.booleanValue();
    }

    public boolean isGpsEnabled() {
        return gpsEnabled.booleanValue();
    }

    @Override
    public String toString() {
        return "ClientContext [ktClientId=" + clientUserId + ", authenticated=" + authenticated + ", authenticationResponse=" + authenticationResponse
                + ", mustReset=" + mustReset + ", gpsEnabled=" + gpsEnabled + "]";
    }

}// class
