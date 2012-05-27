package com.kit.lightserver.services.db.authenticate;

import java.util.LinkedList;
import java.util.List;

import com.fap.framework.db.QueryBooleanParameter;
import com.fap.framework.db.QueryParameter;
import com.fap.framework.db.QueryStringParameter;
import com.fap.framework.db.UpdateQueryInterface;

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
