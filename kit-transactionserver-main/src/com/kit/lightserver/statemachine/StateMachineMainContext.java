package com.kit.lightserver.statemachine;

import com.jfap.framework.configuration.ConfigAccessor;
import com.kit.lightserver.adapters.adapterout.ClientAdapterOut;
import com.kit.lightserver.domain.types.ConnectionInfo;
import com.kit.lightserver.statemachine.types.ClientInfoCTX;

public final class StateMachineMainContext {

    private final ClientAdapterOut clientAdapterOut;

    private final ConnectionInfo connectionInfo;

    private ClientInfoCTX clientInfo = null;

    private final ConfigAccessor configAccessor;

    public StateMachineMainContext(final ClientAdapterOut clientAdapterOut, final ConfigAccessor configAccessor, final ConnectionInfo connectionId) {
        this.clientAdapterOut = clientAdapterOut;
        this.configAccessor = configAccessor;
        this.connectionInfo = connectionId;
    }// constructor

    public ClientAdapterOut getClientAdapterOut() {
        return clientAdapterOut;
    }

    public ConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }

    public ConfigAccessor getConfigAccessor() {
        return configAccessor;
    }

    public ClientInfoCTX getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(final ClientInfoCTX clientInfo) {
        if (this.clientInfo != null) {
            throw new RuntimeException("clientInfo already assigned.");
        }
        this.clientInfo = clientInfo;
    }

}// class
