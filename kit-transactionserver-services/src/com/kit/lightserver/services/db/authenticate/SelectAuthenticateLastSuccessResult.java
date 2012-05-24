package com.kit.lightserver.services.db.authenticate;

public final class SelectAuthenticateLastSuccessResult {

    private final boolean available;
    private final int lastVersion;
    private final String lastInstallationIdAb;

    public SelectAuthenticateLastSuccessResult() {
        this.available = false;
        this.lastInstallationIdAb = null;
        this.lastVersion = -1;
    }

    public SelectAuthenticateLastSuccessResult(final String lastInstallationIdAb, final int lastVersion) {
        this.available = true;
        this.lastInstallationIdAb = lastInstallationIdAb;
        this.lastVersion = lastVersion;
    }

    public boolean isAvailable() {
        return available;
    }

    public int getLastVersion() {
        return lastVersion;
    }

    public String getLastInstallationIdAb() {
        return lastInstallationIdAb;
    }

}// class
