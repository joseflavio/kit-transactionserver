package com.kit.lightserver.config;

import org.dajo.configuration.ConfigAccessor;
import org.dajo.framework.filepath.DajoFilepath;
import org.dajo.framework.filepath.DajoFilepathAdapter;
import org.dajo.types.IntegerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kit.lightserver.services.be.authentication.AuthenticationService;


public final class ServerConfig {

    static private final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    static private final DajoFilepath DEFAULT_TRAY_ICON = new DajoFilepath(DajoFilepath.Type.CLASS_PATH, "/images/kit-icone-azul.png");

    static public ServerConfig getInstance(final ConfigAccessor configAccessor) {
        final ServerConfig serverConfiguration = new ServerConfig(configAccessor);
        LOGGER.info("Configuration loaded. serverConfiguration="+serverConfiguration);
        return serverConfiguration;
    }

    private final String serverName;
    private final int serverPort;
    private final DajoFilepath serverIcon;

    private ServerConfig(final ConfigAccessor accessor) {
        this.serverName = accessor.getMandatoryProperty("server.name");
        this.serverPort = accessor.getMandatoryProperty("server.port", new IntegerAdapter()).intValue();
        this.serverIcon = accessor.getOptionalProperty("server.icon", new DajoFilepathAdapter(DajoFilepath.Type.FILESYSTEM), DEFAULT_TRAY_ICON);
    }

    public String getServerName() {
        return serverName;
    }

    public int getServerPort() {
        return serverPort;
    }

    public DajoFilepath getServerIcon() {
        return serverIcon;
    }

    @Override
    public String toString() {
        return "ServerConfig [serverName=" + serverName + ", serverPort=" + serverPort + ", serverIcon=" + serverIcon + "]";
    }

}// class