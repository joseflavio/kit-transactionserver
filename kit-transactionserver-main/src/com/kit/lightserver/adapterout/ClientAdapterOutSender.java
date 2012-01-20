package com.kit.lightserver.adapterout;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import kit.primitives.base.Primitive;
import kit.primitives.factory.PrimitiveStreamFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class ClientAdapterOutSender {

    static private final Logger LOGGER = LoggerFactory.getLogger(ClientAdapterOutSender.class);

    private final DataOutputStream dataOutputStream;

    private boolean validToSend = false;

    public ClientAdapterOutSender(final Socket socket) {

        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            validToSend = true;
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

    }// constructor

    final void sendToTheClientSocket(final Primitive clientPrimitive) {
        try {
            LOGGER.info("Sending clientPrimitive=" + clientPrimitive);
            PrimitiveStreamFactory.writePrimitive(dataOutputStream, clientPrimitive);
        } catch (final IOException e) {
            closeOutput();
            LOGGER.error("Could not send. clientPrimitive=" + clientPrimitive, e);
        }
    }

    void closeOutput() {
        validToSend = false;
        try {
            dataOutputStream.close();
            LOGGER.info("Data output stream is closed.");
        } catch (final IOException e) {
            LOGGER.error("Error closing the dataOutputStream.", e);
        }
    }

    boolean isValidToSend() {
        return validToSend;
    }

}// class
