package com.kit.lightserver;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfap.framework.configuration.ConfigAccessor;
import com.jfap.framework.configuration.ConfigurationReader;


public final class KITLightServerBootstrap {

    static private final Logger LOGGER = LoggerFactory.getLogger(KITLightServerBootstrap.class);

    static public void main(final String[] args) {

        KITLightServerBootstrap bootstrap = new KITLightServerBootstrap();
        bootstrap.start();
        LOGGER.info("Init finished.");

    }

    private final ConfigAccessor configAccessor;
    private final TrayIcon trayIcon;
    private final KITLightServer kitLightServer;

    private KITLightServerBootstrap() {

        this.configAccessor = ConfigurationReader.getConfiguration();
        this.kitLightServer = new KITLightServer(1000, configAccessor);

        Runtime.getRuntime().addShutdownHook(new ShutdownThread());

        if (!SystemTray.isSupported()) {
            throw new RuntimeException("SystemTray is not supported");
        }

        URL imageIconJarUrl = KITLightServerBootstrap.class.getResource("/images/kit.png");
        ImageIcon imageIcon = new ImageIcon(imageIconJarUrl);

        final PopupMenu popup = new PopupMenu();
        MenuItem aboutItem = new MenuItem("Sobre");

        MenuItem shutDownItem = new MenuItem("Desligar o servidor");

        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(shutDownItem);

        this.trayIcon = new TrayIcon(imageIcon.getImage());
        trayIcon.setPopupMenu(popup);
        trayIcon.setImageAutoSize(true);

        final SystemTray tray = SystemTray.getSystemTray();
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            throw new RuntimeException("TrayIcon could not be added");
        }

        trayIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                JOptionPane.showMessageDialog(null, "O servidor KeepInTouch esta executando.");
            }
        });

        shutDownItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                stopServer();
            }
        });

    }// constructor

    private void start() {
        String threadName = "T0:main-XXXXXXXXXXXXXX";
        Thread thread = new Thread(kitLightServer, threadName);
        thread.start();
    }

    final class ShutdownThread extends Thread {
        @Override
        public void run() {
            LOGGER.warn("Shutdown hook triggered.");
            stopServer();
        }
    }// class

    private void stopServer() {
        LOGGER.info("Server is shutting down.");
        final SystemTray tray = SystemTray.getSystemTray();
        tray.remove(trayIcon);
        kitLightServer.stopServer();
    }

}// class
