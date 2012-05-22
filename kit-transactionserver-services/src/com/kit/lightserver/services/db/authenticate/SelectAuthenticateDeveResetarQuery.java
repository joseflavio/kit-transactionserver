package com.kit.lightserver.services.db.authenticate;

import java.util.LinkedList;
import java.util.List;

import com.fap.framework.db.QueryParameter;
import com.fap.framework.db.QueryStringParameter;
import com.fap.framework.db.SelectQueryInterface;

final class SelectAuthenticateDeveResetarQuery implements SelectQueryInterface {

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    SelectAuthenticateDeveResetarQuery(final String ktClientId) {

        final QueryStringParameter ktClientIdParam = new QueryStringParameter(ktClientId);
        queryParameters.add(ktClientIdParam);

    }// constructor

    @Override
    public String getPreparedSelectQueryString() {

        final String selectQueryStr = "SELECT KTDeveResetar FROM "
                + TableAuthenticateConstants.TABLE_AUTHENTICATE_DEVERESETAR + " WHERE KTClientId=?";

        return selectQueryStr;

    }

    @Override
    public List<QueryParameter> getSelectQueryParameters() {
        return queryParameters;
    }

}// class
