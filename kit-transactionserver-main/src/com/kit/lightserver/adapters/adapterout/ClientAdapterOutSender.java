package com.kit.lightserver.adapters.adapterout;

import java.io.DataOutputStream;
import java.io.IOException;

import kit.primitives.base.Primitive;
import kit.primitives.factory.PrimitiveStreamFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kit.lightserver.adapters.logger.AdaptersLogger;
import com.kit.lightserver.network.SocketWrapper;

final class ClientAdapterOutSender {

    static private final Logger LOGGER = LoggerFactory.getLogger(ClientAdapterOutSender.class);

    private final DataOutputStream dataOutputStream;

    private boolean validToSend = false;

    private final SocketWrapper socketWrapper;

    public ClientAdapterOutSender(final SocketWrapper socketWrapper) {
        this.socketWrapper = socketWrapper;
        this.dataOutputStream = socketWrapper.getDataOutputStream();
        this.validToSend = true;

    }// constructor

    final void sendToTheClientSocket(final Primitive clientPrimitive) {
        try {
            AdaptersLogger.logSending(clientPrimitive);
            PrimitiveStreamFactory.writePrimitive(dataOutputStream, clientPrimitive);
        } catch (final IOException e) {
            closeOutput();
            LOGGER.error("Could not send. clientPrimitive=" + clientPrimitive, e);
        }
    }

    void closeOutput() {
        validToSend = false;
        socketWrapper.closeDataOutputStream();
    }

    boolean isValidToSend() {
        return validToSend;
    }

}// class
