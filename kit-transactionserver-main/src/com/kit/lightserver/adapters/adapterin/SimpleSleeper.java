package com.kit.lightserver.adapters.adapterin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class SimpleSleeper {

    static private final Logger LOGGER = LoggerFactory.getLogger(SimpleSleeper.class);

    static public void sleep(final int sleepTimeInMillis) {

        try {
            Thread.sleep(sleepTimeInMillis);
        }
        catch (InterruptedException e) {
            LOGGER.error("Could not sleep.", e);
        }// try-catch

    }

}// class
