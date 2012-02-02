package com.kit.lightserver.services.db.authenticate;

import com.kit.lightserver.domain.types.ConnectionInfo;
import com.kit.lightserver.domain.types.InstallationIdSTY;
import com.kit.lightserver.services.db.InsertQueryExecuter;
import com.kit.lightserver.services.db.InsertQueryResult;

public final class TableLogConexoesOperations {

    static public InsertQueryResult registerConnection(final ConnectionInfo connectionInfo, final InstallationIdSTY installationIdSTY, final String ktClientId,
            final int status) {

        final InsertLogConexoesQuery insertQuery = new InsertLogConexoesQuery(installationIdSTY, ktClientId, status, connectionInfo);

        final InsertQueryResult result = InsertQueryExecuter.executeInsertQuery(insertQuery);

        return result;

    }

}// class
