package com.kit.lightserver.services.db.authenticate;

import java.util.LinkedList;
import java.util.List;

import com.fap.framework.db.QueryParameter;
import com.fap.framework.db.UpdateQueryInterface;
import com.kit.lightserver.services.db.QueryBooleanParameter;
import com.kit.lightserver.services.db.QueryStringParameter;

final class UpdateAuthenticateUserLogOffQuery implements UpdateQueryInterface {

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    public UpdateAuthenticateUserLogOffQuery(final String ktClientId, final boolean mustResetInNextConnection) {

        QueryBooleanParameter ktDeveResetar = new QueryBooleanParameter(mustResetInNextConnection);
        QueryStringParameter ktClientIdParam = new QueryStringParameter(ktClientId);

        queryParameters.add(ktDeveResetar);
        queryParameters.add(ktClientIdParam);

    }// constructor

    @Override
    public String getPreparedUpdateQueryString() {
        final String queryStr =
                "UPDATE " + TableAuthenticateConstants.TABLE_AUTHENTICATE +
                " SET KTLastDisconnectionDbDateTime=GETDATE(), KTDeveResetar=? WHERE KTClientId=?";
        return queryStr;
    }

    @Override
    public List<QueryParameter> getUpdateQueryParameters() {
        return queryParameters;
    }

}// class
