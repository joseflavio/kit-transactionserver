package com.kit.lightserver.services.be.authentication;

import com.kit.lightserver.domain.types.InstallationIdAbVO;

public final class AuthenticateLastSuccessfulServiceResponse {

    static public final AuthenticateLastSuccessfulServiceResponse FAILED_DATABASE_ERROR = new AuthenticateLastSuccessfulServiceResponse();

    private final boolean success;

    private final InstallationIdAbVO lastInstallationIdAb;

    private final int lastVersion;

    public AuthenticateLastSuccessfulServiceResponse() {
        this.success = false;
        this.lastInstallationIdAb = null;
        this.lastVersion = -1;
    }

    public AuthenticateLastSuccessfulServiceResponse(final InstallationIdAbVO lastInstallationIdAb, final int lastVersion) {
        this.success = true;
        this.lastInstallationIdAb = lastInstallationIdAb;
        this.lastVersion = lastVersion;
    }

    public boolean isSuccess() {
        return success;
    }

    public InstallationIdAbVO getLastInstallationIdAb() {
        return lastInstallationIdAb;
    }

    public int getLastVersion() {
        return lastVersion;
    }

}// class
