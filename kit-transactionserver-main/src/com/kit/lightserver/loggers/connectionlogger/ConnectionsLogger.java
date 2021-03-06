package com.kit.lightserver.loggers.connectionlogger;

import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kit.lightserver.domain.types.ConnectionInfoVO;

public final class ConnectionsLogger {

    static private final Logger LOGGER = LoggerFactory.getLogger(ConnectionsLogger.class);

    static public void logConnection(final ConnectionInfoVO connectionInfo, final InetAddress clientInetAddress) {
        LOGGER.info("[CONNECTION ACCEPTED] connectionInfo="+connectionInfo+", clientInetAddress="+clientInetAddress);
    }

    static public void logConnectionClosed(final ConnectionInfoVO connectionInfo) {
        LOGGER.info("[CONNECTION CLOSED  ] connectionInfo="+connectionInfo);
    }

}// class
