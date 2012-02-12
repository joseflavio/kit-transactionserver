package com.kit.lightserver;


public final class KITLightServerBootstrap {

    static public void main(final String[] args) {

        KITLightServer kitLightServer = new KITLightServer(1000);
        kitLightServer.startServer();

        Runtime.getRuntime().addShutdownHook(new ShutdownThread(kitLightServer));

    }

    static final class ShutdownThread extends Thread {
        private final KITLightServer kitLightServer;
        public ShutdownThread(final KITLightServer kitLightServer) {
            this.kitLightServer = kitLightServer;
        }
        @Override
        public void run() {
            kitLightServer.stopServer();
        }
    }// class

}// class
