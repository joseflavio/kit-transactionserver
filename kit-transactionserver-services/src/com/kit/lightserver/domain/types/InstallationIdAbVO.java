package com.kit.lightserver.domain.types;

public final class InstallationIdAbVO {

    private final long installationId1;
    private final long installationId2;

    public InstallationIdAbVO(final long installationId1, final long installationId2) {
        this.installationId1 = installationId1;
        this.installationId2 = installationId2;
    }

    public long getInstallationId1() {
        return installationId1;
    }

    public long getInstallationId2() {
        return installationId2;
    }

    @Override
    public String toString() {
        return "InstallationIdSTY [" + Long.toHexString(installationId1) + ":" + Long.toHexString(installationId2) + "]";
    }

    static public String toDBString(final InstallationIdAbVO installationIdSTY) {
        final String idAStr = Long.toHexString(installationIdSTY.getInstallationId1());
        final String idBStr = Long.toHexString(installationIdSTY.getInstallationId2());
        final String idABStr = idAStr + ":" + idBStr;
        return idABStr;
    }

}// class
