package com.kit.lightserver.services.db.authenticate;

import java.util.LinkedList;
import java.util.List;

import com.fap.framework.db.QueryParameter;
import com.fap.framework.db.SelectQueryInterface;
import com.kit.lightserver.services.db.QueryStringParameter;

final class SelectClientIdAndPasswordQuery implements SelectQueryInterface {

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    SelectClientIdAndPasswordQuery(final String ktClientId) {

        final QueryStringParameter ktClientIdParam = new QueryStringParameter(ktClientId);
        queryParameters.add(ktClientIdParam);

    }// constructor

    @Override
    public String getPreparedSelectQueryString() {

        final String selectQueryStr = "SELECT KTClientId, KTPassword, KTDeveResetar FROM "
                + TableAuthenticateConstants.TABLE_AUTHENTICATE + " WHERE KTClientId=?";

        return selectQueryStr;

    }

    @Override
    public List<QueryParameter> getSelectQueryParameters() {
        return queryParameters;
    }

}// class
