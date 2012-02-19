package com.kit.lightserver;

import com.jfap.framework.configuration.ConfigAccessor;
import com.jfap.framework.configuration.ConfigurationReader;
import com.kit.lightserver.KITLightServerBootstrap.ShutdownThread;

final class KITLightServerTestMain {

    static public void main(final String[] args) {

        ConfigAccessor configAccessor = ConfigurationReader.getConfiguration();

        KITLightServer kitLightServer = new KITLightServer(40000, configAccessor);
        kitLightServer.waitConnection();

        Runtime.getRuntime().addShutdownHook(new ShutdownThread(kitLightServer));

    }



}// class
