package com.kit.lightserver;

import br.pro.danielferber.bootstrap.BootstrapHandler;

public final class KITLightServerBootstrap implements BootstrapHandler {

    private final KITLightServer kitLightServer = new KITLightServer(500);

    @Override
    public void startServer() throws Exception {
        kitLightServer.startServer();
    }

    @Override
    public void stopServer() throws Exception {
        kitLightServer.stopServer();
    }

}// class
