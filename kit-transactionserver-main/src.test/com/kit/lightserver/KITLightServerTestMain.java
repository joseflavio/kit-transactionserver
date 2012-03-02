package com.kit.lightserver;

import java.io.IOException;
import java.net.ServerSocket;

import com.jfap.framework.configuration.ConfigAccessor;
import com.jfap.framework.configuration.ConfigurationReader;
import com.jfap.framework.exception.LogUncaughtExceptionHandler;

final class KITLightServerTestMain {

    static private final int SERVER_PORT = 3003;

    static public void main(final String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
        serverSocket.setSoTimeout(40000);

        ConfigAccessor configAccessor = ConfigurationReader.getConfiguration(KitPropertiesFiles.SERVER_PROPERTIES, KitPropertiesFiles.DATABASE_PROPERTIES);

        KITLightServer kitLightServer = new KITLightServer(SERVER_PORT, 40000, configAccessor, new LogUncaughtExceptionHandler());
        kitLightServer.waitConnection2(serverSocket);

    }

}// class
