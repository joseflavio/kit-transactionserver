package com.kit.lightserver.services.db.log;

import java.util.LinkedList;
import java.util.List;

import com.fap.framework.db.InsertQueryInterface;
import com.fap.framework.db.QueryParameter;
import com.fap.framework.db.QueryStringParameter;

final class InsertLogConexoesFinalizadasQuery implements InsertQueryInterface {

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    public InsertLogConexoesFinalizadasQuery(final String clientUserId) {
        QueryStringParameter ktClientIdParam = new QueryStringParameter(clientUserId);
        queryParameters.add(ktClientIdParam);
    }// constructor

    @Override
    public String getPreparedInsertQueryString() {
        final String queryStr =
                "INSERT " + TableLogConexoesConstants.TABLE_LOG_CONEXOES_FINALIZADAS +
                " SET LogConexoesFinalizadas=GETDATE() WHERE KTClientId=?";
        return queryStr;
    }

    @Override
    public List<QueryParameter> getInsertQueryParameters() {
        return queryParameters;
    }

}// class
