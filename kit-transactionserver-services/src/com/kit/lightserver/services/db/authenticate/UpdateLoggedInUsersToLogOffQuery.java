package com.kit.lightserver.services.db.authenticate;

import java.util.LinkedList;
import java.util.List;

import com.kit.lightserver.services.db.QueryParameter;
import com.kit.lightserver.services.db.UpdateQueryInterface;

final class UpdateLoggedInUsersToLogOffQuery implements UpdateQueryInterface {

    private final List<QueryParameter> noQueryParameters = new LinkedList<QueryParameter>();

    public UpdateLoggedInUsersToLogOffQuery() {
    }// constructor

    @Override
    public String getPreparedUpdateQueryString() {

        final String queryStr =
                "UPDATE " + TableAuthenticateConstants.TABLE_AUTHENTICATE +
                " SET KTUsuarioConectado=0, KTLastDisconnectionDbDateTime=NULL, KTDeveResetar=1 WHERE KTUsuarioConectado=1";

        return queryStr;

    }

    @Override
    public List<QueryParameter> getUpdateQueryParameters() {
        return noQueryParameters;
    }

}// class
