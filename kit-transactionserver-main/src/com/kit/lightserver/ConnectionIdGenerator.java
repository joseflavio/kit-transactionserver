package com.kit.lightserver;

import java.net.InetAddress;
import java.util.Date;
import java.util.UUID;

import com.kit.lightserver.domain.types.ConnectionId;

final class ConnectionIdGenerator {

    public static ConnectionId createNewConnectionId(final InetAddress clientAddress) {
        final UUID connectionUUID = UUID.randomUUID();
        final Date connectionDate = new Date();
        final String clientHostAddress = clientAddress.getHostAddress();
        return new ConnectionId(connectionUUID, connectionDate, clientHostAddress);
    }

}// class
