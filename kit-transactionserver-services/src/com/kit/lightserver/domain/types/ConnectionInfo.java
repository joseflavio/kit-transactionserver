package com.kit.lightserver.domain.types;


public final class ConnectionInfo {

    private final String connectionUniqueId;
    private final String clientHostAddress;

    ConnectionInfo(final String connectionUniqueId, final String clientHostAddress) {
        this.connectionUniqueId = connectionUniqueId;
        this.clientHostAddress = clientHostAddress;
    }

    public String getConnectionUniqueId() {
        return connectionUniqueId;
    }

    public String getClientHostAddress() {
        return clientHostAddress;
    }

    @Override
    public String toString() {
        return "ConnectionInfo [connectionUniqueId=" + connectionUniqueId + ", clientHostAddress=" + clientHostAddress + "]";
    }

}// class
