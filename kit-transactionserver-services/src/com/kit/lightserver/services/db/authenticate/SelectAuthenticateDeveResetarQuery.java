package com.kit.lightserver.services.db.authenticate;

import java.util.LinkedList;
import java.util.List;

import org.dajo.framework.db.QueryParameter;
import org.dajo.framework.db.QueryStringParameter;
import org.dajo.framework.db.SelectQueryInterface;

final class SelectAuthenticateDeveResetarQuery implements SelectQueryInterface {

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    SelectAuthenticateDeveResetarQuery(final String ktClientId) {

        final QueryStringParameter ktClientIdParam = new QueryStringParameter(ktClientId);
        queryParameters.add(ktClientIdParam);

    }// constructor

    @Override
    public String getPreparedSelectQueryString() {

        final String selectQueryStr = "SELECT [KTClientMustReset] FROM "
                + TableAuthenticateConstants.TABLE_AUTHENTICATE_DEVERESETAR + " WHERE KTClientUserId=?";

        return selectQueryStr;

    }

    @Override
    public List<QueryParameter> getSelectQueryParameters() {
        return queryParameters;
    }

}// class
