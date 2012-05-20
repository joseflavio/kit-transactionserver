package com.kit.lightserver.adapters.adapterout;

import java.io.DataOutputStream;
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

    private final KitServerDataOutput<DataOutputStream> socketDataOutput;

    private boolean validToSend = false;

    private final SocketWrapper socketWrapper;

    public ClientAdapterOutSender(final SocketWrapper socketWrapper) {
        this.socketWrapper = socketWrapper;
        this.socketDataOutput = new KitServerDataOutput<>(socketWrapper.getDataOutputStream());
        this.validToSend = true;

    }// constructor

    final boolean sendToTheClientSocket(final List<Primitive> primitiveList) {
        try {
            for(Primitive primitive : primitiveList) {
                AdaptersLogger.logSending(primitive);
                PrimitiveStreamFactory.writePrimitive(socketDataOutput, primitive);
            }
            socketDataOutput.flush();
            return true;
        } catch (final IOException e) {
            LOGGER.error("Could not send. primitiveList=" + primitiveList, e);
            return false;
        }
    }

    void closeOutput() {
        socketWrapper.closeDataOutputStream();
        validToSend = false;
        LOGGER.info("socketDataOutput.getTotalBytesSent()="+socketDataOutput.getTotalBytesSent());
        LOGGER.info("socketDataOutput.getFlushCount()="+socketDataOutput.getFlushCount());
    }

    boolean isValidToSend() {
        return validToSend;
    }

}// class
