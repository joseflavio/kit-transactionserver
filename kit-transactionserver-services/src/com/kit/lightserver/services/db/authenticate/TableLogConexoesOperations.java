package com.kit.lightserver.services.db.authenticate;

import com.fap.framework.db.DatabaseConfig;
import com.fap.framework.db.InsertQueryExecuter;
import com.fap.framework.db.InsertQueryResult;
import com.kit.lightserver.domain.types.ConnectionInfo;
import com.kit.lightserver.domain.types.InstallationIdSTY;

public final class TableLogConexoesOperations {

    private final DatabaseConfig dbConfig;

    public TableLogConexoesOperations(final DatabaseConfig dataSource) {
        this.dbConfig = dataSource;
    }

    public InsertQueryResult registerConnection(final ConnectionInfo connectionInfo, final InstallationIdSTY installationIdSTY, final String ktClientId,
            final int status) {

        final InsertLogConexoesQuery insertQuery = new InsertLogConexoesQuery(installationIdSTY, ktClientId, status, connectionInfo);

        final InsertQueryResult result = InsertQueryExecuter.executeInsertQuery(dbConfig, insertQuery);

        return result;

    }

}// class
