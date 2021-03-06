package com.kit.lightserver;

import javax.swing.JOptionPane;

import org.dajo.configuration.ConfigAccessor;
import org.dajo.configuration.ConfigurationReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class KitTransactionServerMain {

    static private final Logger LOGGER = LoggerFactory.getLogger(KitTransactionServerMain.class);

    static public void main(final String[] args) {
        KitTransactionServerGui server = null;
        //KitTransactionServerHttpMain httpsServer = null;
        try {
            ConfigAccessor configAcessor = ConfigurationReader.loadExternalProperties(KitPropertiesFiles.SERVER_PROPERTIES, KitPropertiesFiles.DATABASE_PROPERTIES);
            server = new KitTransactionServerGui(configAcessor);
            //httpsServer = new KitTransactionServerHttpMain(configAcessor, "res/kit-transactionserver-dev-keystore.jks", "Kit45$buZZzL#");
            server.start();
            //httpsServer.start();
            LOGGER.info("Init finished.");
        } catch (Throwable t) {
            JOptionPane.showMessageDialog(null, "Um erro inexperado occoreu, por favor consulte os arquivos de log. (t.getClass()="+t.getClass()+", t="+t+")");
            if( server != null ) {
                server.stopServer(true);
            }
            //if( httpsServer != null ) { httpsServer.stop(); }
        }
    }


}// class
