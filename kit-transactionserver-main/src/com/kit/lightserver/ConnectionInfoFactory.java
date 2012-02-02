package com.kit.lightserver;

import java.net.InetAddress;

import com.jfap.framework.connectionid.ConnectionIdGenerator;
import com.kit.lightserver.domain.types.ConnectionInfo;

final class ConnectionInfoFactory {

    public static ConnectionInfo getInstance(final InetAddress clientAddress) {
        final String connectionUniqueId = ConnectionIdGenerator.generateRandomConnectionId();
        final String clientHostAddress = clientAddress.getHostAddress();
        return new ConnectionInfo(connectionUniqueId, clientHostAddress);
    }

}// class
