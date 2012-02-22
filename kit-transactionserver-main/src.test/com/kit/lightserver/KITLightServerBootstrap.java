package com.kit.lightserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfap.framework.configuration.ConfigAccessor;
import com.jfap.framework.configuration.ConfigurationReader;


public final class KITLightServerBootstrap {

    static private final Logger LOGGER = LoggerFactory.getLogger(KITLightServerBootstrap.class);

    static public void main(final String[] args) {

        ConfigAccessor configAccessor = ConfigurationReader.getConfiguration();

        KITLightServer kitLightServer = new KITLightServer(1000, configAccessor);
        String threadName = "T0:main-XXXXXXXXXXXXXX";
        Thread thread = new Thread(kitLightServer, threadName);
        thread.start();

        Runtime.getRuntime().addShutdownHook(new ShutdownThread(kitLightServer));

        LOGGER.info("Init finished.");

    }

    static final class ShutdownThread extends Thread {
        private final KITLightServer kitLightServer;
        public ShutdownThread(final KITLightServer kitLightServer) {
            this.kitLightServer = kitLightServer;
        }
        @Override
        public void run() {
            LOGGER.warn("Shutdown hook triggered.");
            kitLightServer.stopServer();
        }
    }// class

}// class
