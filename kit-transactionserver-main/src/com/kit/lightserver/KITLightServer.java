package com.kit.lightserver;

import java.io.IOException;
import java.io.InputStream;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.Channels;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfap.framework.configuration.ConfigAccessor;
import com.jfap.framework.exception.LogUncaughtExceptionHandler;
import com.kit.lightserver.adapters.adapterin.AdiClientListenerRunnable;
import com.kit.lightserver.domain.types.ConnectionInfo;
import com.kit.lightserver.domain.types.ConnectionInfoFactory;
import com.kit.lightserver.loggers.connectionlogger.ConnectionsLogger;
import com.kit.lightserver.network.JavaNetSocketWrapper;
import com.kit.lightserver.network.SocketWrapper;

public final class KITLightServer implements Runnable {

    static private final Logger LOGGER = LoggerFactory.getLogger(KITLightServer.class);

    private final ConfigAccessor configAccessor;

    static private final int SERVER_PORT = 3301;

    private boolean isAlive = true;

    private final int socketTimeout;

    public KITLightServer(final int socketTimeout, final ConfigAccessor configAccessor) {

        this.socketTimeout = socketTimeout;
        this.configAccessor = configAccessor;

        /*
         * TO log any unexpected exception
         */
        Thread.setDefaultUncaughtExceptionHandler(new LogUncaughtExceptionHandler());

    }// constructor

    /**
     * Entry-point
     */
    @Override
    public void run() {

        listenForConnections();

    }

    static public void listenForConnections2() {
        try {
            final AsynchronousServerSocketChannel serverSocket = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(SERVER_PORT));
            AsynchronousSocketChannel socket = serverSocket.accept().get();
            InputStream is = Channels.newInputStream(socket);
            is.close();
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void listenForConnections() {

        /*
         * Init the ServerSocket
         */
        final ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            serverSocket.setSoTimeout(socketTimeout);
            LOGGER.info("Server socket created. serverPort="+SERVER_PORT+", socketTimeout="+socketTimeout);
        }
        catch (final IOException e) {
            LOGGER.error("Unexpected error! Could not start the server. serverPort=" + SERVER_PORT, e);
            throw new RuntimeException(e);
        }

        /*
         * Accepting income connections
         */
        while (isAlive) {
            waitConnection2(serverSocket);
        }

        /*
         * Close the ServerSocket
         */
        try {
            serverSocket.close();
        }
        catch (final IOException e) {
            LOGGER.error("Unexpected error. serverPort=" + SERVER_PORT, e);
            throw new RuntimeException(e);
        }

        LOGGER.info("Not waiting for new connections. serverPort=" + SERVER_PORT);
    }

    public void waitConnection2(final ServerSocket serverSocket) {
        try {

            Socket clientSocket = serverSocket.accept(); // Blocks waiting to a Client connect
            InetAddress clientInetAddress = clientSocket.getInetAddress();

            final ConnectionInfo connectionInfo = ConnectionInfoFactory.getInstance(clientInetAddress); //It creates a unique ID for the connection
            LOGGER.info("Connection accepted. connectionInfo=" + connectionInfo);
            ConnectionsLogger.logConnection(connectionInfo, clientInetAddress);

            /*
             * Forking a thread to deal with the external connection
             */
            SocketWrapper socketWrapper = new JavaNetSocketWrapper(clientSocket);
            AdiClientListenerRunnable clientListenerThread = new AdiClientListenerRunnable(socketWrapper, configAccessor, connectionInfo);
            String threadName = "T1:" + connectionInfo.getConnectionUniqueId();
            Thread thread = new Thread(clientListenerThread, threadName);
            thread.start();

        }
        catch (final SocketTimeoutException e) {
            // Expected exception
        }
        catch (final BindException e) {
            LOGGER.error("Could not start the server, port probably already in use.", e);
        }
        catch (final IOException e) {
            LOGGER.error("Unexpected error. serverPort=" + SERVER_PORT, e);
            throw new RuntimeException(e);
        }
    }

    public void stopServer() {
        LOGGER.info("Server is stopping.");
        isAlive = false;
    }

}// class
