package com.kit.lightserver.services.db.connectionlog;

import com.fap.framework.db.KitDataSource;
import com.fap.framework.db.UpdateQueryResult;

public final class TableLogConexoesOperations {

    private final KitDataSource dataSource;

    public TableLogConexoesOperations(final KitDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public UpdateQueryResult updateLastDisconnection(final String userClientId) {
        UpdateAuthenticateLastDisconnectionQuery updateQuery = new UpdateAuthenticateLastDisconnectionQuery(userClientId);
        UpdateQueryResult result = dataSource.executeUpdateQuery(updateQuery);
        return result;
    }

}// class
