package com.kit.lightserver;

import java.lang.Thread.UncaughtExceptionHandler;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fap.framework.db.DatabaseConfig;
import com.jfap.framework.configuration.ConfigAccessor;
import com.jfap.framework.configuration.ConfigurationReader;
import com.kit.lightserver.config.ServerConfig;
import com.kit.lightserver.gui.traymenu.KitTrayMenu;


public final class KITLightServerGui {

    static private final Logger LOGGER = LoggerFactory.getLogger(KITLightServerGui.class);



    private final ConfigAccessor configAccessor;
    private final ServerConfig serverConfig;
    private final DatabaseConfig dbConfig;
    private final KITLightServer kitLightServer;
    private final KitTrayMenu trayMenu;

    KITLightServerGui() {

        configAccessor = ConfigurationReader.getConfiguration(KitPropertiesFiles.SERVER_PROPERTIES, KitPropertiesFiles.DATABASE_PROPERTIES);
        serverConfig = ServerConfig.getInstance(configAccessor);
        dbConfig = DatabaseConfig.getInstance(configAccessor);

        kitLightServer = new KITLightServer(serverConfig.getServerPort(), 1000, configAccessor, new BootstrapUncaughtExceptionHandler() );

        trayMenu = new KitTrayMenu(serverConfig, dbConfig, new KitTrayIconListeners());
        trayMenu.install();

        Runtime.getRuntime().addShutdownHook(new ShutdownThread());

    }// constructor

    void start() {
        String threadName = "T0:main-XXXXXXXXXXXXXX";
        Thread thread = new Thread(kitLightServer, threadName);
        thread.start();
    }

    private void stopServer() {
        LOGGER.info("Server is shutting down.");
        kitLightServer.stopServer();
        trayMenu.uninstall();
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
            stopServer();
        }
    }// class

    final class ShutdownThread extends Thread {
        @Override
        public void run() {
            LOGGER.warn("Shutdown hook triggered.");
            stopServer();
        }
    }// class

    public class KitTrayIconListeners {
        public void desligarServidor() {
            stopServer();
        }
    }// class

}// class
