package com.kit.lightserver.services.db.authenticate;

import com.fap.framework.db.KitDataSource;
import com.fap.framework.db.InsertQueryResult;
import com.kit.lightserver.domain.types.ConnectionInfo;
import com.kit.lightserver.domain.types.InstallationIdSTY;

public final class TableLogConexoesOperations {

    private final KitDataSource dataSource;

    public TableLogConexoesOperations(final KitDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public InsertQueryResult registerConnection(final ConnectionInfo connectionInfo, final InstallationIdSTY installationIdSTY, final String ktClientId,
            final int status) {

        InsertLogConexoesQuery insertQuery = new InsertLogConexoesQuery(installationIdSTY, ktClientId, status, connectionInfo);
        InsertQueryResult result = dataSource.executeInsertQuery(insertQuery);
        return result;

    }

}// class
