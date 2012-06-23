package com.fap.framework.exception;

import java.lang.Thread.UncaughtExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final public class LogUncaughtExceptionHandler implements UncaughtExceptionHandler {

    static private final Logger LOGGER = LoggerFactory.getLogger(LogUncaughtExceptionHandler.class);

    @Override
    public void uncaughtException(final Thread t, final Throwable e) {
        LOGGER.error("Uncaught Exception in thread="+t.getName(), e);
    }

}// class


