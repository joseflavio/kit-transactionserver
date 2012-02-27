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
import com.kit.lightserver.services.be.authentication.DatabaseConfig;

public class KitTrayMenu {

    static private final Logger LOGGER = LoggerFactory.getLogger(KitTrayMenu.class);

    private final TrayIcon trayIcon;

    public KitTrayMenu(final ServerConfig serverConfig, final DatabaseConfig dbConfig, final KitTrayIconListeners kitTrayIconListeners) {

        if (!SystemTray.isSupported()) {
            throw new RuntimeException("SystemTray is not supported");
        }

        ImageIcon imageIcon = ImageIconLoader.load(serverConfig.getServerIcon());

        PopupMenu popup = new PopupMenu();
        MenuItem serverNameItem = new MenuItem(serverConfig.getServerName() + ", porta " + serverConfig.getServerPort());
        MenuItem databaseNameItem = new MenuItem("Database: " + dbConfig.getDbName());
        MenuItem goToLogsItem = new MenuItem("Ir para pasta de Logs");
        MenuItem shutDownItem = new MenuItem("Desligar o servidor");

        popup.add(serverNameItem);
        popup.addSeparator();
        popup.add(databaseNameItem);
        popup.addSeparator();
        popup.add(goToLogsItem);
        popup.addSeparator();
        popup.add(shutDownItem);

        this.trayIcon = new TrayIcon(imageIcon.getImage());
        trayIcon.setPopupMenu(popup);
        trayIcon.setImageAutoSize(true);

        trayIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Servidor KeepInTouch esta executando.");
            }
        });

        goToLogsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                KitTrayMenu.launchExplorerInLogsDirectory();
            }
        });

        shutDownItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                kitTrayIconListeners.desligarServidor();
            }
        });

    }

    static protected void launchExplorerInLogsDirectory() {
        File currentDir = new File("logs");
        String currentFullDir = currentDir.getAbsolutePath();
        ProcessBuilder processBuilder = new ProcessBuilder("explorer.exe", currentFullDir);
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
