package com.fap.framework.uniqueids;

import java.util.UUID;

public final class LastConnectionTokenGenerator {

    static public long generateRandomConnectionId() {

        final UUID uuid = UUID.randomUUID();
        final long least = uuid.getLeastSignificantBits();
        final long most = uuid.getMostSignificantBits();

        final long token = least ^ most;

        return token;

    }

}// class
