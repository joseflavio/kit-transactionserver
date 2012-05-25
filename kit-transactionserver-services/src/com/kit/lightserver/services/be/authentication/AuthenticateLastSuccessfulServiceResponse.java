package com.kit.lightserver.services.be.authentication;

import com.kit.lightserver.domain.types.InstallationIdAbVO;

public final class AuthenticateLastSuccessfulServiceResponse {

    static public final AuthenticateLastSuccessfulServiceResponse FAILED_DATABASE_ERROR = new AuthenticateLastSuccessfulServiceResponse();

    private final boolean success;

    private final InstallationIdAbVO lastInstallationIdAb;

    private String lastConnectionUniqueId;

    private final int lastVersion;

    private AuthenticateLastSuccessfulServiceResponse() {
        this.success = false;
        this.lastInstallationIdAb = null;
        this.lastVersion = -1;
    }

    public AuthenticateLastSuccessfulServiceResponse(final InstallationIdAbVO lastInstallationIdAb, final String lastConnectionUniqueId, final int lastVersion) {
        this.success = true;
        this.lastInstallationIdAb = lastInstallationIdAb;
        this.lastConnectionUniqueId = lastConnectionUniqueId;
        this.lastVersion = lastVersion;
    }

    public String getLastConnectionUniqueId() {
        return lastConnectionUniqueId;
    }

    public void setLastConnectionUniqueId(final String lastConnectionUniqueId) {
        this.lastConnectionUniqueId = lastConnectionUniqueId;
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
