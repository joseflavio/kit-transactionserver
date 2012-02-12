package com.kit.lightserver;

import com.kit.lightserver.KITLightServerBootstrap.ShutdownThread;

final class KITLightServerTestMain {

    static public void main(final String[] args) {

        KITLightServer kitLightServer = new KITLightServer(40000);
        kitLightServer.listenForNewConnections();

        Runtime.getRuntime().addShutdownHook(new ShutdownThread(kitLightServer));

    }



}// class
