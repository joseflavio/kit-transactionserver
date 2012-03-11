package com.kit.lightserver.adapters.adapterout;

import java.io.IOException;
import java.util.List;

import kit.primitives.base.Primitive;
import kit.primitives.factory.PrimitiveStreamFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kit.lightserver.adapters.logger.AdaptersLogger;
import com.kit.lightserver.network.SocketWrapper;

final class ClientAdapterOutSender {

    static private final Logger LOGGER = LoggerFactory.getLogger(ClientAdapterOutSender.class);

    private final ServerDataWriter dataOutputStream;

    private boolean validToSend = false;

    private final SocketWrapper socketWrapper;

    public ClientAdapterOutSender(final SocketWrapper socketWrapper) {
        this.socketWrapper = socketWrapper;
        this.dataOutputStream = new ServerDataWriter(socketWrapper.getDataOutputStream());
        this.validToSend = true;

    }// constructor

    final boolean sendToTheClientSocket(final List<Primitive> primitiveList) {
        try {
            AdaptersLogger.logSending(primitiveList);
            PrimitiveStreamFactory.writePrimitive(dataOutputStream, primitiveList);
            return true;
        } catch (final IOException e) {
            LOGGER.error("Could not send. primitiveList=" + primitiveList, e);
            return false;
        }
    }

    void closeOutput() {
        LOGGER.info("dataOutputStream.getTotalBytesSent="+dataOutputStream.getTotalBytesSent());
        validToSend = false;
        socketWrapper.closeDataOutputStream();
    }

    boolean isValidToSend() {
        return validToSend;
    }

}// class
