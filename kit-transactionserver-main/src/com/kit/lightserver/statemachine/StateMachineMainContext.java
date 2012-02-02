package com.kit.lightserver.statemachine;

import com.kit.lightserver.adapters.adapterout.ClientAdapterOut;
import com.kit.lightserver.domain.types.ConnectionInfo;
import com.kit.lightserver.statemachine.types.ClientInfoCTX;

public final class StateMachineMainContext {

    private final ClientAdapterOut clientAdapterOut;

    private final ConnectionInfo connectionId;

    private ClientInfoCTX clientInfo = null;

    public StateMachineMainContext(final ClientAdapterOut clientAdapterOut, final ConnectionInfo connectionId) {
        this.clientAdapterOut = clientAdapterOut;
        this.connectionId = connectionId;
    }// constructor

    public ClientAdapterOut getClientAdapterOut() {
        return clientAdapterOut;
    }

    public ConnectionInfo getConnectionId() {
        return connectionId;
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
