package com.jfap.framework.connectionid;

import java.util.UUID;

public final class ConnectionIdGenerator {

    static public String generateRandomConnectionId() {

        UUID uuid = UUID.randomUUID();
        long least = uuid.getLeastSignificantBits();
        long most = uuid.getMostSignificantBits();

        long shortUuid = least ^ most;
        String shortUuidString = Long.toHexString(shortUuid).toUpperCase();

        return shortUuidString;

    }

}// class
