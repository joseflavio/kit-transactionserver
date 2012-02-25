package com.kit.lightserver;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Main {

    static private final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(final String[] args) {
        try {
            KITLightServerGui bootstrap = new KITLightServerGui();
            bootstrap.start();
            LOGGER.info("Init finished.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Um erro inexperado occoreu, por favor consulte os arquivos de log. (e="+e.getMessage()+")");
        }

    }

}
