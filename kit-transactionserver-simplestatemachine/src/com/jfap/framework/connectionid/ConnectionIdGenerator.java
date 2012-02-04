package com.jfap.framework.connectionid;

import java.util.UUID;

public final class ConnectionIdGenerator {

    static public String generateRandomConnectionId() {

        UUID uuid = UUID.randomUUID();
        long least = uuid.getLeastSignificantBits();
        long most = uuid.getMostSignificantBits();

        long shortUuid = least ^ most;
        String shortUuidString = Long.toHexString(shortUuid).toUpperCase();
        if( shortUuidString.length() < 16 ) {
            System.err.println("UUID too short. most="+most);
            System.err.println("UUID too short. least="+least);
            System.err.println("UUID too short. shortUuid="+shortUuid);
            System.err.println("UUID too short. shortUuidString="+shortUuidString);
        }

        String separatedId =
                    shortUuidString.substring(0, 4) + "-" +
                    shortUuidString.substring(4, 8) + "-" +
                    shortUuidString.substring(8, 12) + "-" +
                    shortUuidString.substring(12, 16);

        return separatedId;

    }

}// class
