package com.kit.lightserver.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JavaNetSocketWrapper implements SocketWrapper {

    static private final Logger LOGGER = LoggerFactory.getLogger(JavaNetSocketWrapper.class);

    private final Socket clientSocket;
    private boolean clientSocketClosed = false;

    private final DataOutputStream dataOutputStream;
    private boolean canCloseDataOutputStream = false;

    private final DataInputStream dataInputStream;
    private boolean dataInputStreamIsClosed = false;

    public JavaNetSocketWrapper(final Socket clientSocket) {
        try {
            this.clientSocket = clientSocket;
            this.dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            this.dataInputStream = new DataInputStream(clientSocket.getInputStream());
        }
        catch (IOException e) {
            throw new RuntimeException("Unexpected Error.", e);
        }
    }

    @Override
    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    @Override
    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    @Override
    public void closeDataInputStream() {
        try {
            dataInputStream.close();
        }
        catch (IOException e) {
            LOGGER.error("Unexpected error.", e);
        }
        LOGGER.info("dataInputStream is closed.");
        dataInputStreamIsClosed = true;
        checkForCloseSocket();
    }

    @Override
    public void closeDataOutputStream() {
        canCloseDataOutputStream = true;
        checkForCloseSocket();
    }

    @Override
    public boolean dataOutputCanBeClosed() {
        return canCloseDataOutputStream;
    }

    private void checkForCloseSocket() {
        if (dataInputStreamIsClosed == true && canCloseDataOutputStream == true) {

            /*
             * The order should be respected:
             * 1 - The InputStream
             * 2 - The OutputStream
             * 3 - The Socket
             */
            try {
                dataOutputStream.close();
            }
            catch (final IOException e) {
                LOGGER.error("Unexpected error.", e);
            }
            LOGGER.info("dataOutputStream is closed.");

            try {
                clientSocket.close();
            }
            catch (IOException e) {
                LOGGER.error("Unexpected error.", e);
            }
            clientSocketClosed = true;
            LOGGER.info("clientSocket is closed.");
        }
    }

    @Override
    public boolean isClosed() {
        return clientSocketClosed;
    }



}// class
