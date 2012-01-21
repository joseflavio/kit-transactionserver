package com.kit.lightserver.services.db.authenticate;

import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;

import com.kit.lightserver.domain.types.ConnectionId;
import com.kit.lightserver.domain.types.InstallationIdSTY;
import com.kit.lightserver.services.db.InsertQueryInterface;
import com.kit.lightserver.services.db.QueryDateTimeParameter;
import com.kit.lightserver.services.db.QueryIntegerParameter;
import com.kit.lightserver.services.db.QueryParameter;
import com.kit.lightserver.services.db.QueryStringParameter;

final class InsertLogConexoesQuery implements InsertQueryInterface {

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    public InsertLogConexoesQuery(final ConnectionId connectionId, final InstallationIdSTY installationIdSTY, final String ktClientId, final int status,
            final int ano, final int mes, final int dia, final DateTime dateTime, final String localDateStr) {

        final String idAStr = Long.toHexString(installationIdSTY.getInstallationId1());
        final String idBStr = Long.toHexString(installationIdSTY.getInstallationId2());
        final String idABStr = idAStr + ":" + idBStr;

        final QueryStringParameter ktConexaoIdParam = new QueryStringParameter(connectionId.getConnectionIdStr());
        final QueryStringParameter idAParam = new QueryStringParameter(idAStr);
        final QueryStringParameter idBParam = new QueryStringParameter(idBStr);
        final QueryStringParameter idABParam = new QueryStringParameter(idABStr);
        final QueryStringParameter ktClientIdParam = new QueryStringParameter(ktClientId);
        final QueryIntegerParameter statusParam = new QueryIntegerParameter(status);
        final QueryIntegerParameter ktDataAnoParam = new QueryIntegerParameter(ano);
        final QueryIntegerParameter ktDataMesParam = new QueryIntegerParameter(mes);
        final QueryIntegerParameter ktDataDiaParam = new QueryIntegerParameter(dia);
        final QueryDateTimeParameter ktDataJavaServerNative = new QueryDateTimeParameter(dateTime);
        final QueryStringParameter ktDataJavaServerString = new QueryStringParameter(localDateStr);

        queryParameters.add(ktConexaoIdParam);
        queryParameters.add(idAParam);
        queryParameters.add(idBParam);
        queryParameters.add(idABParam);
        queryParameters.add(ktClientIdParam);
        queryParameters.add(statusParam);
        queryParameters.add(ktDataAnoParam);
        queryParameters.add(ktDataMesParam);
        queryParameters.add(ktDataDiaParam);
        queryParameters.add(ktDataJavaServerNative);
        queryParameters.add(ktDataJavaServerString);

    }// constructor


    @Override
    public String getPreparedInsertQueryString() {

        final String queryStr =
                "INSERT INTO " + TableLogConexoesConstants.TABLE_LOG_CONEXOES
                + " (KTRowInsertDbDateTime, KTConexaoID, KTCelularIdA, KTCelularIdB, KTCelularIdAB, KTClientID, KTStatusDaConexao, KTRowInsertJDateAno, KTRowInsertJDateMes, KTRowInsertJDateDia, KTRowInsertJDateTimeNative, KTRowInsertJDateTimeString) values (GETDATE(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        return queryStr;

    }

    @Override
    public List<QueryParameter> getInsertQueryParameters() {
        return queryParameters;
    }

}// class
