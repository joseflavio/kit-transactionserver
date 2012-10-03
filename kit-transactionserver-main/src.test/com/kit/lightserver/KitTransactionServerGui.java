package com.kit.lightserver;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import org.dajo.configuration.ConfigAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kit.lightserver.config.ServerConfig;
import com.kit.lightserver.gui.traymenu.KitTrayMenu;


public final class KitTransactionServerGui {

    static private final Logger LOGGER = LoggerFactory.getLogger(KitTransactionServerGui.class);

    private final ConfigAccessor configAccessor;
    private final ServerConfig serverConfig;
    private final KITLightServer kitLightServer;
    private final KitTrayMenu trayMenu;

    private final List<ShutdownHandler> shutDownHandlers = new LinkedList<ShutdownHandler>();
    private boolean shuttingDown = false;

    KitTransactionServerGui(final ConfigAccessor configAcessor) {

        this.configAccessor = configAcessor;

        serverConfig = ServerConfig.getInstance(configAccessor);

        String dbaHost = this.configAccessor.getMandatoryProperty("database.dba.host");
        String dbaPort = this.configAccessor.getMandatoryProperty("database.dba.port");

        String dbaDesc = "DBA: " + dbaHost + ":" + dbaPort;

        kitLightServer = new KITLightServer(serverConfig.getServerPort(), 1000, configAccessor, new BootstrapUncaughtExceptionHandler() );

        trayMenu = new KitTrayMenu(serverConfig, dbaDesc, new KitTrayIconListeners());
        trayMenu.install();

        Runtime.getRuntime().addShutdownHook(new ShutdownThread());

    }// constructor

    void start() {
        String threadName = "T0:main-XXXXXXXXXXXXXX";
        Thread thread = new Thread(kitLightServer, threadName);
        thread.start();
    }

    public synchronized boolean stopServer(final boolean removeTrayIcon) {
        if (shuttingDown == false) {
            shuttingDown = true;
            LOGGER.info("Server is shutting down...");
            try {
                kitLightServer.stopServer();
                for (ShutdownHandler handler : shutDownHandlers) {
                    handler.onShutdown();
                }
                if( removeTrayIcon == true ) {
                    trayMenu.uninstall();
                }
            }
            catch (Throwable t) {
                LOGGER.info("Error shutting down.", t);
            }
            LOGGER.info("Shutdown done.");
            return true;
        }
        return false;
    }

    public void addShutdownHandler(final ShutdownHandler handler) {
        shutDownHandlers.add(handler);
    }

    final class BootstrapUncaughtExceptionHandler implements UncaughtExceptionHandler {
        @Override
        public void uncaughtException(final Thread t, final Throwable e) {
            if( e instanceof ServerPortNotAvailableException ) {
                ServerPortNotAvailableException nae = (ServerPortNotAvailableException) e;
                JOptionPane.showMessageDialog(null, "A porta " + nae.getServerPort() + " ja esta sendo utilizada. O servidor não pode iniciar");
            }
            else {
                JOptionPane.showMessageDialog(null, "Um erro inexperado occoreu, por favor consulte os arquivos de log. (e="+e.getMessage()+")");
            }
            stopServer(true);
        }
    }// class

    final class ShutdownThread extends Thread {
        @Override
        public void run() {
            LOGGER.info("Shutdown hook triggered.");
            boolean stopServerResult = stopServer(false);
            LOGGER.info("Shutdown hook stopServerResult="+stopServerResult);
            if( stopServerResult == true ) {
                LOGGER.warn("Shutdown hook triggered by not normal event.");
            }
        }
    }// class

    public class KitTrayIconListeners {
        public void desligarServidor() {
            stopServer(true);
        }
    }// class

}// class
