package com.kit.lightserver;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

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

    private final int serverPort;
    private final int socketTimeout;
    private final ConfigAccessor configAccessor;

    private boolean isAlive = true;

    public KITLightServer(final int serverPort, final int socketTimeout, final ConfigAccessor configAccessor) {

        this.serverPort = serverPort;
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

//    static public void listenForConnections2() {
//        try {
//            final AsynchronousServerSocketChannel serverSocket = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(SERVER_PORT));
//            AsynchronousSocketChannel socket = serverSocket.accept().get();
//            InputStream is = Channels.newInputStream(socket);
//            is.close();
//        }
//        catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

    public void listenForConnections() {

        /*
         * Init the ServerSocket
         */
        final ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(serverPort);
            serverSocket.setSoTimeout(socketTimeout);
            LOGGER.info("Server socket created. serverPort="+serverPort+", socketTimeout="+socketTimeout);
        }
        catch (final IOException e) {
            LOGGER.error("Unexpected error! Could not start the server. serverPort=" + serverPort, e);
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
            LOGGER.error("Unexpected error.", e);
            throw new RuntimeException(e);
        }

        LOGGER.info("Not waiting for new connections.");
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
            LOGGER.error("Unexpected error." , e);
        }

    }

    public void stopServer() {
        LOGGER.info("Server is stopping.");
        isAlive = false;
    }

}// class
