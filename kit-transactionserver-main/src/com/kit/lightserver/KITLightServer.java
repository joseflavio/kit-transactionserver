package com.kit.lightserver;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kit.lightserver.adapterin.ClientAdapterInListenerThread;
import com.kit.lightserver.domain.types.ConnectionId;
import com.kit.lightserver.services.be.authentication.AuthenticationService;

public final class KITLightServer {

    static private final Logger LOGGER = LoggerFactory.getLogger(KITLightServer.class);

    private final int serverPort = 3003;

    private boolean isAlive = true;

    private final ServerSocket serverSocket;

    public KITLightServer(final int poolingTimeOutInMillis) {

        boolean authenticationServiceInitSuccess = AuthenticationService.initAndRecoverIfNecessary();
        if( authenticationServiceInitSuccess == false ) {
            final String errorMessage = "An essencial service could not init. authenticationServiceInitSuccess="+authenticationServiceInitSuccess;
            LOGGER.error(errorMessage);
            throw new RuntimeException(errorMessage);
        }

        try {
            this.serverSocket = new ServerSocket(serverPort);
            this.serverSocket.setSoTimeout(poolingTimeOutInMillis);
            LOGGER.info("Server socket created. serverPort="+serverPort);
        }
        catch (final IOException e) {
            LOGGER.error("Unexpected error! Could not start the server. serverPort=" + serverPort, e);
            throw new RuntimeException(e);
        }

    }// constructor

    /**
     * Server entry-point
     */
    public void startServer() {

        while (isAlive) {
            listenForNewConnections();
        }// while

        try {
            serverSocket.close();
        }
        catch (final IOException e) {
            LOGGER.error("Unexpected error. serverPort=" + serverPort, e);
            throw new RuntimeException(e);
        }

        LOGGER.info("Not listening for new connections in serverPort=" + serverPort);

    }

    public void listenForNewConnections() {
        try {
            final Socket clientSocket = serverSocket.accept(); // Blocks waiting to a Client connect
            final InetAddress clientAddress = clientSocket.getInetAddress();
            final ConnectionId connectionId = ConnectionIdGenerator.createNewConnectionId(clientAddress); // Socket starts so we create a unique ID for it
            LOGGER.info("Connection accepted. clientAddress=" + clientAddress);
            final ClientAdapterInListenerThread clientListenerThread = new ClientAdapterInListenerThread(clientSocket, connectionId);
            final Thread thread = new Thread(clientListenerThread);
            thread.start();
            isAlive = false;
        }
        catch (final SocketTimeoutException e) {
            // Expected exception
        }
        catch (final BindException e) {
            LOGGER.error("Could not start the server, port probably already in use.", e);
        }
        catch (final IOException e) {
            LOGGER.error("Unexpected error. serverPort=" + serverPort, e);
            throw new RuntimeException(e);
        }
    }

    public void stopServer() {
        isAlive = false;
    }

}// class
