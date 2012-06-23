package com.kit.lightserver.gui.traymenu;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kit.lightserver.KITLightServerGui.KitTrayIconListeners;
import com.kit.lightserver.config.ServerConfig;
import com.kit.lightserver.gui.resources.ImageIconLoader;

public final class KitTrayMenu {

    static private final Logger LOGGER = LoggerFactory.getLogger(KitTrayMenu.class);

    private final TrayIcon trayIcon;

    private final File currentDir = new File("logs");
    private final String currentFullDir = currentDir.getAbsolutePath();
    private final ProcessBuilder processBuilder = new ProcessBuilder("explorer.exe", currentFullDir);

    private final long startTime;

    public KitTrayMenu(final ServerConfig serverConfig, final String dbDescription, final KitTrayIconListeners kitTrayIconListeners) {

        startTime = System.currentTimeMillis();

        if ( SystemTray.isSupported() == false ) {
            throw new RuntimeException("SystemTray is not supported");
        }

        ImageIcon imageIcon = ImageIconLoader.load(serverConfig.getServerIcon());

        PopupMenu popup = new PopupMenu();
        MenuItem serverNameItem = new MenuItem(serverConfig.getServerName() + ", porta " + serverConfig.getServerPort());
        MenuItem databaseNameItem = new MenuItem("Database: " + dbDescription);
        MenuItem goToLogsItem = new MenuItem("Ir para pasta de Logs");
        MenuItem shutDownItem = new MenuItem("Desligar o servidor");

        popup.add(serverNameItem);
        popup.addSeparator();
        popup.add(databaseNameItem);
        popup.addSeparator();
        popup.add(goToLogsItem);
        popup.addSeparator();
        popup.add(shutDownItem);

        trayIcon = new TrayIcon(imageIcon.getImage());
        trayIcon.setPopupMenu(popup);
        trayIcon.setImageAutoSize(true);

        trayIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                displayMessage();
            }
        });

        goToLogsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                launchExplorerInLogsDirectory();
            }
        });

        shutDownItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                kitTrayIconListeners.desligarServidor();
            }
        });

    }

    protected void displayMessage() {
        long runningTime = System.currentTimeMillis() - startTime;
        long runningTimeInMinutes = runningTime / 60000;
        JOptionPane.showMessageDialog(null, "Servidor KeepInTouch esta executando por "+runningTimeInMinutes+" minutos.");
    }

    private void launchExplorerInLogsDirectory() {
        try {
            processBuilder.start();
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error tentando iniciar o explorer (verifique se o explorer.exe esta no seu PATH).");
            LOGGER.error("Error launching explorer.exe", e);
        }
    }

    public void install() {
        final SystemTray tray = SystemTray.getSystemTray();
        try {
            tray.add(trayIcon);
        }
        catch (AWTException e) {
            throw new RuntimeException("TrayIcon could not be added");
        }
    }

    public void uninstall() {
        final SystemTray tray = SystemTray.getSystemTray();
        tray.remove(trayIcon);
    }

}// class
