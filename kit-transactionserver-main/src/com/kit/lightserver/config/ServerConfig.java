package com.kit.lightserver.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfap.framework.configuration.ConfigAccessor;
import com.jfap.framework.configuration.IntegerAdapter;
import com.kit.lightserver.services.be.authentication.AuthenticationService;


public final class ServerConfig {

    static private final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    static private final String DEFAULT_TRAY_ICON = "/images/kit-icone-azul.png";

    static public ServerConfig getInstance(final ConfigAccessor configAccessor) {
        final ServerConfig serverConfiguration = new ServerConfig(configAccessor);
        LOGGER.info("Configuration loaded. serverConfiguration="+serverConfiguration);
        return serverConfiguration;
    }

    //static private final ConfigurationAccessor CONFIG = ConfigurationReader.getConfiguration(DatabaseConfiguration.class);

    private final String serverName;
    private final int serverPort;
    private final String serverIcon;

    private ServerConfig(final ConfigAccessor accessor) {
        this.serverName = accessor.getMandatoryProperty("server.name");
        this.serverPort = accessor.getMandatoryProperty("server.port", new IntegerAdapter()).intValue();
        this.serverIcon = accessor.getOptionalProperty("server.icon", DEFAULT_TRAY_ICON);
    }

    public String getServerName() {
        return serverName;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getServerIcon() {
        return serverIcon;
    }

    @Override
    public String toString() {
        return "ServerConfig [serverName=" + serverName + ", serverPort=" + serverPort + ", serverIcon=" + serverIcon + "]";
    }

}// class