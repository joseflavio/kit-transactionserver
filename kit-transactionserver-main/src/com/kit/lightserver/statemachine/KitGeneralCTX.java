package com.kit.lightserver.statemachine;

import com.kit.lightserver.adapterout.ClientAdapterOut;
import com.kit.lightserver.services.types.ConnectionId;

public final class KitGeneralCTX {

    private final ClientAdapterOut clientAdapterOut;

    private final ConnectionId connectionId;

    private ClientInfoCTX clientInfo = null;

    public KitGeneralCTX(final ClientAdapterOut clientAdapterOut, final ConnectionId connectionId) {
        this.clientAdapterOut = clientAdapterOut;
        this.connectionId = connectionId;
    }// constructor

    public ClientAdapterOut getClientAdapterOut() {
        return clientAdapterOut;
    }

    public ConnectionId getConnectionId() {
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
