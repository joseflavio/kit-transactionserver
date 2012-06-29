package com.kit.lightserver.services.db.dbl;

import java.util.LinkedList;
import java.util.List;

import org.dajo.framework.db.InsertQueryInterface;
import org.dajo.framework.db.QueryIntParameter;
import org.dajo.framework.db.QueryParameter;
import org.dajo.framework.db.QueryStringParameter;

import com.kit.lightserver.domain.types.ConnectionInfoVO;

final class InsertLogConexoesFinalizadasQuery implements InsertQueryInterface {

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    public InsertLogConexoesFinalizadasQuery(final String clientUserId, final ConnectionInfoVO connectionInfo) {

        final String connectionIdStr = connectionInfo.getConnectionUniqueId();

        QueryStringParameter clientUserIdParam = new QueryStringParameter(clientUserId);
        QueryStringParameter connectionIdParam = new QueryStringParameter(connectionIdStr);
        QueryIntParameter connectionEndStatusParam = new QueryIntParameter(666);
        QueryIntParameter connectionTotalTimeParam = new QueryIntParameter(120);
        QueryIntParameter formsSentParam = new QueryIntParameter(5);
        QueryIntParameter formsReceivedParam = new QueryIntParameter(15);
        QueryIntParameter bytesSentParam = new QueryIntParameter(1024);
        QueryIntParameter bytesReceivedParam = new QueryIntParameter(2048);
        QueryIntParameter primitivesSentParam = new QueryIntParameter(50);
        QueryIntParameter primitivesReceivedParam = new QueryIntParameter(100);

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
                "INSERT INTO " + DBLTables.TABLE_LOG_CONEXOES_FINALIZADAS +
                "( [KTClientUserId], [KTConnectionId], [KTConnectionEndStatus], [KTConnectionTotalTime]," +
                "  [KTFormsSent], [KTFormsReceived], [KTBytesSent], [KTBytesReceived], [KTPrimitivesSent], [KTPrimitivesReceived] ) VALUES " +
                "( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";

        return queryStr;

    }

    @Override
    public List<QueryParameter> getInsertQueryParameters() {
        return queryParameters;
    }

}// class
