package com.kit.lightserver.gui.traymenu;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.kit.lightserver.KITLightServerGui.KitTrayIconListeners;
import com.kit.lightserver.config.ServerConfig;
import com.kit.lightserver.gui.resources.ImageIconLoader;
import com.kit.lightserver.services.be.authentication.DatabaseConfig;

public class KitTrayMenu {

    private final TrayIcon trayIcon;

    public KitTrayMenu(final ServerConfig serverConfig, final DatabaseConfig dbConfig, final KitTrayIconListeners kitTrayIconListeners) {

       if (!SystemTray.isSupported()) {
           throw new RuntimeException("SystemTray is not supported");
       }

       ImageIcon imageIcon = ImageIconLoader.load(serverConfig.getServerIcon()) ;

       final PopupMenu popup = new PopupMenu();
       MenuItem serverNameItem = new MenuItem(serverConfig.getServerName()+", porta "+serverConfig.getServerPort());
       MenuItem databaseNameItem = new MenuItem(dbConfig.getDbName());
       MenuItem shutDownItem = new MenuItem("Desligar o servidor");

       popup.add(serverNameItem);
       popup.add(databaseNameItem);
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

       shutDownItem.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(final ActionEvent e) {
               kitTrayIconListeners.desligarServidor();
           }
       });

    }

    public void install() {
        final SystemTray tray = SystemTray.getSystemTray();
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            throw new RuntimeException("TrayIcon could not be added");
        }
    }

    public void uninstall() {
        final SystemTray tray = SystemTray.getSystemTray();
        tray.remove(trayIcon);
    }

}// class
