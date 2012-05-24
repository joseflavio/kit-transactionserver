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

    static public InstallationIdAbVO fromDbString(final String lastInstallationIdAbStr) {
        try {
            final String[] parts = lastInstallationIdAbStr.split("\\:");
            final long idA = InstallationIdAbVO.properParseLong(parts[0]);
            final long idB = InstallationIdAbVO.properParseLong(parts[1]);
            return new InstallationIdAbVO(idA, idB);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static private long properParseLong(final String s) {
        final String least = s.substring(0, 8);
        final String most = s.substring(8, 16);
        return (Long.parseLong(least, 16)<<32) | Long.parseLong(most, 16);
    }

}// class
