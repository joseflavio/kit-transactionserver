package com.kit.lightserver;

import java.net.BindException;

public class ServerPortNotAvailableException extends RuntimeException {

    static private final long serialVersionUID = -841669945472905847L;

    private final int serverPort;

    public ServerPortNotAvailableException(final int serverPort, final BindException cause) {
        super(cause);
        this.serverPort = serverPort;
    }

    public int getServerPort() {
        return serverPort;
    }

}// class
