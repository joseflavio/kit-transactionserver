package com.kit.lightserver;

final class KITLightServerTestMain {

    static public void main(final String[] args) {

        KITLightServer kitLightServer = new KITLightServer(40000);
        kitLightServer.listenForNewConnections();

    }

}// class
