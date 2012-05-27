package com.kit.lightserver.domain.types;

public final class InstallationIdAbVO {

    private final String idABStr;

    public InstallationIdAbVO(final long installationId1, final long installationId2) {
        final String idAStr = Long.toHexString(installationId1).toUpperCase();
        final String idBStr = Long.toHexString(installationId2).toUpperCase();
        this.idABStr = idAStr + ":" + idBStr;
    }

    public InstallationIdAbVO(final String idABStr) {
        this.idABStr = idABStr;
    }

    public String getIdABStr() {
        return idABStr;
    }

    @Override
    public String toString() {
        return "InstallationIdAbVO [idABStr=" + idABStr + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idABStr == null) ? 0 : idABStr.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        InstallationIdAbVO other = (InstallationIdAbVO) obj;
        if (idABStr == null) {
            if (other.idABStr != null) {
                return false;
            }
        }
        else if (!idABStr.equals(other.idABStr)) {
            return false;
        }
        return true;
    }

}// class
