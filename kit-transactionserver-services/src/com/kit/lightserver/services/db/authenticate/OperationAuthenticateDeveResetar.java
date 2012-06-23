package com.kit.lightserver.services.db.authenticate;

import org.dajo.framework.db.SimpleQueryExecutor;
import org.dajo.framework.db.UpdateQueryResult;

public final class OperationAuthenticateDeveResetar {

    static public UpdateQueryResult updateMustReset(final SimpleQueryExecutor dbdQueryExecutor, final String clientUserId,
            final boolean mustResetInNextConnection) {

        UpdateAuthenticateDeveResetarQuery updateQuery = new UpdateAuthenticateDeveResetarQuery(clientUserId, mustResetInNextConnection);
        UpdateQueryResult result = dbdQueryExecutor.executeUpdateQuery(updateQuery);

        return result;

    }

}// class
