package com.kit.lightserver.services.db.dbl;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dajo.framework.db.InsertQueryInterface;
import org.dajo.framework.db.QueryIntegerParameter;
import org.dajo.framework.db.QueryParameter;
import org.dajo.framework.db.QueryStringParameter;

import com.kit.lightserver.domain.types.ConnectionInfoVO;
import com.kit.lightserver.domain.types.InstallationIdAbVO;

final class InsertLogConexoesIniciadasQuery implements InsertQueryInterface {

    static private final Logger LOGGER = LoggerFactory.getLogger(InsertLogConexoesIniciadasQuery.class);

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    public InsertLogConexoesIniciadasQuery(final InstallationIdAbVO installationIdSTY, final String clientUserId, final int status,
            final ConnectionInfoVO connectionInfo) {

        final String idABStr = installationIdSTY.getIdABStr();

        final String mobileNetworkAddress = connectionInfo.getClientHostAddress();
        final String properMobileNetworkAddress;
        if( mobileNetworkAddress.length() > 42 ) {
            LOGGER.error("The mobileNetworkAddress is too long. mobileNetworkAddress="+mobileNetworkAddress);
            properMobileNetworkAddress = mobileNetworkAddress.substring(0, 15);
        }
        else {
            properMobileNetworkAddress = mobileNetworkAddress;
        }

        final String connectionUniqueId = connectionInfo.getConnectionUniqueId();

        final QueryStringParameter ktConexaoIdParam = new QueryStringParameter(connectionUniqueId);
        final QueryStringParameter idABParam = new QueryStringParameter(idABStr);
        final QueryStringParameter ktClientIdParam = new QueryStringParameter(clientUserId.toUpperCase());
        final QueryIntegerParameter statusParam = new QueryIntegerParameter( Integer.valueOf(status) );
        final QueryStringParameter networkAddressParam = new QueryStringParameter(properMobileNetworkAddress);

        queryParameters.add(idABParam);
        queryParameters.add(ktClientIdParam);
        queryParameters.add(statusParam);
        queryParameters.add(ktConexaoIdParam);
        queryParameters.add(networkAddressParam);

    }// constructor


    @Override
    public String getPreparedInsertQueryString() {

        final String queryStr =
                "INSERT INTO " + DBLTables.TABLE_LOG_CONEXOES_INICIADAS
                + " ([KTClientInstallIdAB], [KTClientUserId], [KTConnectionAuthStatus], [KTConnectionId], [KTClientNetworkAddress]) VALUES (?, ?, ?, ?, ?)";

        return queryStr;

    }

    @Override
    public List<QueryParameter> getInsertQueryParameters() {
        return queryParameters;
    }

}// class
