package com.kit.lightserver.services.db.authenticate;

import java.util.LinkedList;
import java.util.List;

import org.dajo.framework.db.QueryBooleanParameter;
import org.dajo.framework.db.QueryParameter;
import org.dajo.framework.db.QueryStringParameter;
import org.dajo.framework.db.UpdateQueryInterface;

final class UpdateAuthenticateDeveResetarQuery implements UpdateQueryInterface {

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    public UpdateAuthenticateDeveResetarQuery(final String ktClientId, final boolean mustResetInNextConnection) {

        QueryBooleanParameter ktDeveResetar = new QueryBooleanParameter(Boolean.valueOf(mustResetInNextConnection));
        QueryStringParameter ktClientIdParam = new QueryStringParameter(ktClientId);

        queryParameters.add(ktDeveResetar);
        queryParameters.add(ktClientIdParam);

    }// constructor

    @Override
    public String getPreparedUpdateQueryString() {
        final String queryStr =
                "UPDATE " + TableAuthenticateConstants.TABLE_AUTHENTICATE_DEVERESETAR +
                " SET [KTClientMustReset]=? WHERE [KTClientUserId]=?";
        return queryStr;
    }

    @Override
    public List<QueryParameter> getUpdateQueryParameters() {
        return queryParameters;
    }

}// class
