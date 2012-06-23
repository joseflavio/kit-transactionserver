package com.kit.lightserver.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dajo.framework.adapters.FilepathAdapter;
import org.dajo.framework.adapters.FilepathVO;
import org.dajo.framework.adapters.FilepathVO.FilepathType;
import org.dajo.framework.adapters.IntegerAdapter;
import org.dajo.framework.configuration.ConfigAccessor;

import com.kit.lightserver.services.be.authentication.AuthenticationService;


public final class ServerConfig {

    static private final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    static private final FilepathVO DEFAULT_TRAY_ICON = new FilepathVO(FilepathType.CLASS_PATH, "/images/kit-icone-azul.png");

    static public ServerConfig getInstance(final ConfigAccessor configAccessor) {
        final ServerConfig serverConfiguration = new ServerConfig(configAccessor);
        LOGGER.info("Configuration loaded. serverConfiguration="+serverConfiguration);
        return serverConfiguration;
    }

    //static private final ConfigurationAccessor CONFIG = ConfigurationReader.getConfiguration(DatabaseConfiguration.class);

    private final String serverName;
    private final int serverPort;
    private final FilepathVO serverIcon;

    private ServerConfig(final ConfigAccessor accessor) {
        this.serverName = accessor.getMandatoryProperty("server.name");
        this.serverPort = accessor.getMandatoryProperty("server.port", new IntegerAdapter()).intValue();
        this.serverIcon = accessor.getOptionalProperty("server.icon", new FilepathAdapter(FilepathType.FILESYSTEM), DEFAULT_TRAY_ICON);
    }

    public String getServerName() {
        return serverName;
    }

    public int getServerPort() {
        return serverPort;
    }

    public FilepathVO getServerIcon() {
        return serverIcon;
    }

    @Override
    public String toString() {
        return "ServerConfig [serverName=" + serverName + ", serverPort=" + serverPort + ", serverIcon=" + serverIcon + "]";
    }

}// class