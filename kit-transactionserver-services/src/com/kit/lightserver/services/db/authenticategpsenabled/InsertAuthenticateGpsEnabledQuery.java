package com.kit.lightserver.services.db.authenticategpsenabled;

import java.util.LinkedList;
import java.util.List;

import org.dajo.framework.db.InsertQueryInterface;
import org.dajo.framework.db.QueryParameter;
import org.dajo.framework.db.QueryStringParameter;

final class InsertAuthenticateGpsEnabledQuery implements InsertQueryInterface {

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    InsertAuthenticateGpsEnabledQuery(final String clientUserId) {
        final String uperCaseClientId = clientUserId.toUpperCase();
        final QueryStringParameter ktClientIdParam = new QueryStringParameter(uperCaseClientId);
        queryParameters.add(ktClientIdParam);
    }// constructor

    @Override
    public String getPreparedInsertQueryString() {

        final String insertQueryStr =
                "INSERT INTO " + Constants.TABLE_AUTHENTICATE_GPSENABLED.NAME +
                " ([KTClientUserId], [KTGpsEnabled]) VALUES (?, 0)";

        return insertQueryStr;

    }

    @Override
    public List<QueryParameter> getInsertQueryParameters() {
        return queryParameters;
    }

}// class
