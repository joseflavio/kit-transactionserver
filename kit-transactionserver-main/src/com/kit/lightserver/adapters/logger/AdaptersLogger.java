package com.kit.lightserver.adapters.logger;

import java.util.List;

import kit.primitives.base.Primitive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AdaptersLogger {

    static private final Logger LOGGER = LoggerFactory.getLogger(AdaptersLogger.class);

    static public void logSending(final List<Primitive> primitiveList) {
        for(int i=0; i<primitiveList.size(); i++) {
            Primitive currentPrimitive = primitiveList.get(i);
            final String primitiveName = PrimitiveNameTranslator.getName(currentPrimitive);
            LOGGER.trace("[SEND] name="+primitiveName+", primitive=" + currentPrimitive);
        }
    }

    static public void logReceived(final Primitive primitive) {
        final String name = PrimitiveNameTranslator.getName(primitive);
        LOGGER.trace("[RECV] name="+name+", primitive=" + primitive);
    }

}// class
