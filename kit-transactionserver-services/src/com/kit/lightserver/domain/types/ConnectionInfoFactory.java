package com.kit.lightserver.domain.types;

import java.net.InetAddress;

import com.fap.framework.uniqueids.ConnectionIdGenerator;

public final class ConnectionInfoFactory {

    static public ConnectionInfo getInstance(final InetAddress clientAddress) {
        final String connectionUniqueId = ConnectionIdGenerator.generateRandomConnectionId();
        final String clientHostAddress = clientAddress.getHostAddress();
        return new ConnectionInfo(connectionUniqueId, clientHostAddress);
    }

}// class
