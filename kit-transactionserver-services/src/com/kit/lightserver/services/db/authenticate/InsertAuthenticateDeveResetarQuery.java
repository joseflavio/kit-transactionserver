package com.kit.lightserver.services.db.authenticate;

import java.util.LinkedList;
import java.util.List;

import com.fap.framework.db.InsertQueryInterface;
import com.fap.framework.db.QueryParameter;
import com.fap.framework.db.QueryStringParameter;

final class InsertAuthenticateDeveResetarQuery implements InsertQueryInterface {

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    InsertAuthenticateDeveResetarQuery(final String ktClientId) {
        final String uperCaseClientId = ktClientId.toUpperCase();
        final QueryStringParameter ktClientIdParam = new QueryStringParameter(uperCaseClientId);
        queryParameters.add(ktClientIdParam);

    }// constructor

    @Override
    public String getPreparedInsertQueryString() {

        final String insertQueryStr =
                "INSERT INTO " + TableAuthenticateConstants.TABLE_AUTHENTICATE_DEVERESETAR +
                " ([KTClientId], [KTDeveResetar]) VALUES (?, 0)";

        return insertQueryStr;

    }

    @Override
    public List<QueryParameter> getInsertQueryParameters() {
        return queryParameters;
    }

}// class
