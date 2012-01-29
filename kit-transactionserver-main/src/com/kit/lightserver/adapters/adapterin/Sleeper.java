package com.kit.lightserver.adapters.adapterin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class Sleeper {

    static private final Logger LOGGER = LoggerFactory.getLogger(Sleeper.class);

    private final SleeperAwakeEvent eventToWaitFor;

    public Sleeper(final SleeperAwakeEvent eventToWaitFor) {
        this.eventToWaitFor = eventToWaitFor;
    }// constructor

    public boolean waitFor(final int countLimit) {

        int count = 0;
        boolean interruped = false;
        final long initialTime = System.currentTimeMillis();
        while (eventToWaitFor.shouldWakeUp() == false) {

            try {
                Thread.sleep(1);
            }
            catch (final InterruptedException e) {
                LOGGER.error("Unexpected problem.", e);
            }

            if (count > countLimit) {

                interruped = true;
                break;
            }

            ++count;

        }// while
        final long finalTime = System.currentTimeMillis();
        final long totalWaitingTime = finalTime - initialTime;

        if( interruped == true ) {
            // Failed
            LOGGER.warn("Failed waiting for event. count=" + count + ", event=" + eventToWaitFor + ", totalWaitingTime=" + totalWaitingTime);
            return false;
        }

        LOGGER.info("Success waiting for event. count=" + count + ", event=" + eventToWaitFor + ", totalWaitingTime=" + totalWaitingTime);
        return true;

    }

}// class
