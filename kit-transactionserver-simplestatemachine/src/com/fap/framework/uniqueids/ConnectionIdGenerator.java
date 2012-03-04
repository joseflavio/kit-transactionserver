package com.fap.framework.uniqueids;

import java.util.UUID;

public final class ConnectionIdGenerator {

    static private final String LEADING_ZEROS = "0000000000000000";

    static public String generateRandomConnectionId() {

        UUID uuid = UUID.randomUUID();
        long least = uuid.getLeastSignificantBits();
        long most = uuid.getMostSignificantBits();

        long shortUuid = least ^ most;
        String shortUuidString = Long.toHexString(shortUuid).toUpperCase();

        int diff = 16 - shortUuidString.length();

        if( diff > 0 ) {
            shortUuidString = LEADING_ZEROS.substring(0, diff) + shortUuidString;
        }

        String separatedId =
                    shortUuidString.substring(0, 4) + "-" +
                    shortUuidString.substring(4, 8) + "-" +
                    shortUuidString.substring(8, 12) + "-" +
                    shortUuidString.substring(12, 16);

        return separatedId;

    }

}// class
