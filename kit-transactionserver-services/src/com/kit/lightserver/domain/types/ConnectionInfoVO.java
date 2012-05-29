package com.kit.lightserver.domain.types;

import com.fap.thread.HasThreadSufixName;


public final class ConnectionInfoVO implements HasThreadSufixName {

    private final String connectionUniqueId;
    private final String clientHostAddress;

    ConnectionInfoVO(final String connectionUniqueId, final String clientHostAddress) {
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
    public String getThreadNameSufix() {
        return connectionUniqueId;
    }

    @Override
    public String toString() {
        return "ConnectionInfo [connectionUniqueId=" + connectionUniqueId + ", clientHostAddress=" + clientHostAddress + "]";
    }

}// class
