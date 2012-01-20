package com.kit.lightserver.services.db.authenticate;

import java.util.LinkedList;
import java.util.List;

import com.kit.lightserver.services.db.QueryParameter;
import com.kit.lightserver.services.db.SelectQueryInterface;

final class SelectLoggedInClientIdsQuery implements SelectQueryInterface {

    private final List<QueryParameter> noQueryParameters = new LinkedList<QueryParameter>();

    SelectLoggedInClientIdsQuery() {
    }// constructor

    @Override
    public String getPreparedSelectQueryString() {
        final String selectQueryStr = "SELECT KTClientId FROM " + TableAuthenticateConstants.TABLE_AUTHENTICATE + " WHERE KTUsuarioConectado=1";
        return selectQueryStr;
    }

    @Override
    public List<QueryParameter> getSelectQueryParameters() {
        return noQueryParameters;
    }

}// class
