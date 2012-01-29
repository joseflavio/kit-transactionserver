package com.kit.lightserver.adapters.logger;

import kit.primitives.base.Primitive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AdaptersLogger {

    static private final Logger LOGGER = LoggerFactory.getLogger(AdaptersLogger.class);

    static public void logSending(final Primitive primitive) {
        LOGGER.trace("[SEND] primitive=" + primitive);
    }

    static public void logReceived(final Primitive primitive) {
        LOGGER.trace("[RECV] primitive=" + primitive);
    }

}// class
