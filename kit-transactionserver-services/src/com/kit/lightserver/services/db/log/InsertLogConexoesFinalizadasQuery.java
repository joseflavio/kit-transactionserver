package com.kit.lightserver.services.db.log;

import java.util.LinkedList;
import java.util.List;

import com.fap.framework.db.InsertQueryInterface;
import com.fap.framework.db.QueryIntegerParameter;
import com.fap.framework.db.QueryParameter;
import com.fap.framework.db.QueryStringParameter;

final class InsertLogConexoesFinalizadasQuery implements InsertQueryInterface {

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    public InsertLogConexoesFinalizadasQuery(final String clientUserId) {

        QueryStringParameter clientUserIdParam = new QueryStringParameter(clientUserId);
        QueryStringParameter connectionIdParam = new QueryStringParameter("C");
        QueryIntegerParameter connectionEndStatusParam = new QueryIntegerParameter(666);
        QueryIntegerParameter connectionTotalTimeParam = new QueryIntegerParameter(120);
        QueryIntegerParameter formsSentParam = new QueryIntegerParameter(5);
        QueryIntegerParameter formsReceivedParam = new QueryIntegerParameter(15);
        QueryIntegerParameter bytesSentParam = new QueryIntegerParameter(1024);
        QueryIntegerParameter bytesReceivedParam = new QueryIntegerParameter(2048);
        QueryIntegerParameter primitivesSentParam = new QueryIntegerParameter(50);
        QueryIntegerParameter primitivesReceivedParam = new QueryIntegerParameter(100);

        queryParameters.add(clientUserIdParam);
        queryParameters.add(connectionIdParam);
        queryParameters.add(connectionEndStatusParam);
        queryParameters.add(connectionTotalTimeParam);
        queryParameters.add(formsSentParam);
        queryParameters.add(formsReceivedParam);
        queryParameters.add(bytesSentParam);
        queryParameters.add(bytesReceivedParam);
        queryParameters.add(primitivesSentParam);
        queryParameters.add(primitivesReceivedParam);

    }// constructor

    @Override
    public String getPreparedInsertQueryString() {
        final String queryStr =
                "INSERT INTO " + TableLogConexoesConstants.TABLE_LOG_CONEXOES_FINALIZADAS +
                "( [KTInsertDBTime], [KTClientUserId], [KTConnectionId], [KTConnectionEndStatus], [KTConnectionTotalTime]," +
                "  [KTFormsSent], [KTFormsReceived], [KTBytesSent], [KTBytesReceived], [KTPrimitivesSent], [KTPrimitivesReceived] ) VALUES " +
                "( GETDATE(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";

        return queryStr;
    }

    @Override
    public List<QueryParameter> getInsertQueryParameters() {
        return queryParameters;
    }

}// class
