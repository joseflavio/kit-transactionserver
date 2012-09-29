package com.kit.lightserver.services.db.authenticategpsenabled;

import java.util.LinkedList;
import java.util.List;

import org.dajo.framework.db.QueryParameter;
import org.dajo.framework.db.QueryStringParameter;
import org.dajo.framework.db.SelectQueryInterface;

final class SelectAuthenticateGpsEnabledQuery implements SelectQueryInterface {

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    SelectAuthenticateGpsEnabledQuery(final String clientUserId) {
        final QueryStringParameter ktClientIdParam = new QueryStringParameter(clientUserId);
        queryParameters.add(ktClientIdParam);
    }

    @Override
    public String getPreparedSelectQueryString() {

        final String selectQueryStr = "SELECT [KTGpsEnabled] FROM "
                + Constants.TABLE_AUTHENTICATE_GPSENABLED.NAME + " WHERE KTClientUserId=?";

        return selectQueryStr;

    }

    @Override
    public List<QueryParameter> getSelectQueryParameters() {
        return queryParameters;
    }

}// class
