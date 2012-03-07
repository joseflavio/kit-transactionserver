package com.kit.lightserver.domain.types;

import com.fap.thread.HasThreadName;


public final class ConnectionInfo implements HasThreadName {

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
    public String getThreadName() {
        return connectionUniqueId;
    }

    @Override
    public String toString() {
        return "ConnectionInfo [connectionUniqueId=" + connectionUniqueId + ", clientHostAddress=" + clientHostAddress + "]";
    }

}// class
