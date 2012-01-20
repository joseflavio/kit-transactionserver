package com.kit.lightserver.services.db.authenticate;

import java.util.LinkedList;
import java.util.List;

import com.kit.lightserver.services.db.QueryIntegerParameter;
import com.kit.lightserver.services.db.QueryParameter;
import com.kit.lightserver.services.db.QueryStringParameter;
import com.kit.lightserver.services.db.UpdateQueryInterface;

final class UpdateAuthenticateUserLogInQuery implements UpdateQueryInterface {

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    public UpdateAuthenticateUserLogInQuery(final String ktClientId) {

        QueryIntegerParameter ktUsuarioConectadoParam = new QueryIntegerParameter(1);
        QueryStringParameter ktClientIdParam = new QueryStringParameter(ktClientId);
        queryParameters.add(ktUsuarioConectadoParam);
        queryParameters.add(ktClientIdParam);

    }// constructor

    @Override
    public String getPreparedUpdateQueryString() {
        final String queryStr =
                "UPDATE " + TableAuthenticateConstants.TABLE_AUTHENTICATE +
                " SET KTLastAuthenticationDbDateTime=GETDATE(), KTLastDisconnectionDbDateTime=NULL, KTUsuarioConectado=? WHERE KTClientId=?";
        return queryStr;
    }

    @Override
    public List<QueryParameter> getUpdateQueryParameters() {
        return queryParameters;
    }

}// class
