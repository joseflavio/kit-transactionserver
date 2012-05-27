package com.kit.lightserver.services.db.connectionlog;

import java.util.LinkedList;
import java.util.List;

import com.fap.framework.db.QueryParameter;
import com.fap.framework.db.QueryStringParameter;
import com.fap.framework.db.UpdateQueryInterface;
import com.kit.lightserver.services.db.authenticate.TableAuthenticateConstants;

final class UpdateAuthenticateLastDisconnectionQuery implements UpdateQueryInterface {

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    public UpdateAuthenticateLastDisconnectionQuery(final String userClientId) {
        QueryStringParameter ktClientIdParam = new QueryStringParameter(userClientId);
        queryParameters.add(ktClientIdParam);
    }// constructor

    @Override
    public String getPreparedUpdateQueryString() {
        final String queryStr =
                "UPDATE " + TableAuthenticateConstants.TABLE_AUTHENTICATEPASSWORD +
                " SET KTLastDisconnectionDbDateTime=GETDATE() WHERE KTClientId=?";
        return queryStr;
    }

    @Override
    public List<QueryParameter> getUpdateQueryParameters() {
        return queryParameters;
    }

}// class
