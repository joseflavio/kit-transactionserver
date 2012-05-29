package com.kit.lightserver;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fap.thread.RichThreadFactory;
import com.jfap.framework.configuration.ConfigAccessor;
import com.kit.lightserver.adapters.adapterin.AdiClientListenerRunnable;
import com.kit.lightserver.domain.types.ConnectionInfoFactory;
import com.kit.lightserver.domain.types.ConnectionInfoVO;
import com.kit.lightserver.loggers.connectionlogger.ConnectionsLogger;
import com.kit.lightserver.network.JavaNetSocketWrapper;
import com.kit.lightserver.network.SocketWrapper;
import com.kit.lightserver.statemachine.KITStateMachineRunnable;
import com.kit.lightserver.statemachine.KITStateMachineRunnable.EventQueue;

public final class KITLightServer implements Runnable {

    static private final Logger LOGGER = LoggerFactory.getLogger(KITLightServer.class);

    private final int serverPort;
    private final int socketTimeout;
    private final ConfigAccessor configAccessor;

    private boolean isAlive = true;

    public KITLightServer(final int serverPort, final int socketTimeout, final ConfigAccessor configAccessor,
            final UncaughtExceptionHandler uncaughtExceptionHandler) {

        this.serverPort = serverPort;
        this.socketTimeout = socketTimeout;
        this.configAccessor = configAccessor;

        /*
         * TO log any unexpected exception
         */
        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);

    }// constructor

    /**
     * Entry-point
     */
    @Override
    public void run() {

        listenForConnections();

    }

    // static public void listenForConnections2() {
    // try {
    // final AsynchronousServerSocketChannel serverSocket = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(SERVER_PORT));
    // AsynchronousSocketChannel socket = serverSocket.accept().get();
    // InputStream is = Channels.newInputStream(socket);
    // is.close();
    // }
    // catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }

    public void listenForConnections() {

        // Try to create the socket
        final ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(serverPort);
        }
        catch (final BindException e) {
            LOGGER.error("Unexpected error! Could not start the server. serverPort=" + serverPort, e);
            throw new ServerPortNotAvailableException(serverPort, e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Setting the timeout, so it is not blocking
        try {
            serverSocket.setSoTimeout(socketTimeout);
        }
        catch (SocketException e) {
            throw new RuntimeException(e);
        }

        // Success creating the server socket
        LOGGER.info("Server socket created. serverPort=" + serverPort + ", socketTimeout=" + socketTimeout);

        // Accepting income connections
        while (isAlive) {
            waitConnection2(serverSocket);
        }

        // Closing the server socket
        try {
            serverSocket.close();
        }
        catch (final IOException e) {
            LOGGER.error("Unexpected error.", e);
            throw new RuntimeException(e);
        }

        LOGGER.info("Not waiting for new connections.");

    }

    public void waitConnection2(final ServerSocket serverSocket) {

        try {

            Socket clientSocket = serverSocket.accept(); // Blocks waiting to a Client connect
            InetAddress clientInetAddress = clientSocket.getInetAddress();

            final ConnectionInfoVO connectionInfo = ConnectionInfoFactory.getInstance(clientInetAddress); // It creates a unique ID for the connection
            LOGGER.info("Connection accepted. connectionInfo=" + connectionInfo);
            ConnectionsLogger.logConnection(connectionInfo, clientInetAddress);

            /*
             * Forking a thread to deal with the external connection
             */
            SocketWrapper socketWrapper = new JavaNetSocketWrapper(clientSocket);

            KITStateMachineRunnable kitStateMachineRunnable = new KITStateMachineRunnable(socketWrapper, configAccessor, connectionInfo);
            Thread t2thread = RichThreadFactory.newThread(kitStateMachineRunnable, connectionInfo);
            t2thread.start();

            EventQueue eventQueue = kitStateMachineRunnable.getEventQueue();

            AdiClientListenerRunnable clientListenerRunnable = new AdiClientListenerRunnable(socketWrapper, configAccessor, connectionInfo, eventQueue);
            Thread thread = RichThreadFactory.newThread(clientListenerRunnable, connectionInfo);
            thread.start();

        }
        catch (final SocketTimeoutException e) {
            // Expected exception
        }
        catch (final BindException e) {
            LOGGER.error("Could not start the server, port probably already in use.", e);
            isAlive = false;
        }
        catch (final IOException e) {
            LOGGER.error("Unexpected error.", e);
            isAlive = false;
        }

    }

    public void stopServer() {
        LOGGER.info("Server is stopping.");
        isAlive = false;
    }

}// class
