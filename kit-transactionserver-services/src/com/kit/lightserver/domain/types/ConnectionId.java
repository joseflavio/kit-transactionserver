package com.kit.lightserver.domain.types;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public final class ConnectionId {

    static private final String dateFormat = "yyyyMMdd-HHmm-ss-SSS";



    private final String connectionIdStr;

    public ConnectionId(final UUID connectionUUID, final Date connectionDate, final String clientHostAddress) {
        final String connectionUUIDStr = connectionUUID.toString();
        final DateFormat df = new SimpleDateFormat(dateFormat);
        final String connectionDateStr = df.format(connectionDate);
        final String clientHostAddressValidStr = clientHostAddress.replace('.', '-');
        connectionIdStr = "(" + connectionDateStr + ")-(" + clientHostAddressValidStr + ")-(" + connectionUUIDStr + ")";
    }

    public String getConnectionIdStr() {
        return connectionIdStr;
    }

    @Override
    public String toString() {
        return "ConnectionId [connectionIdStr=" + connectionIdStr + "]";
    }

}// class
