package com.kit.lightserver.services.db.authenticate;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kit.lightserver.domain.types.ConnectionInfo;
import com.kit.lightserver.domain.types.InstallationIdSTY;
import com.kit.lightserver.services.db.InsertQueryInterface;
import com.kit.lightserver.services.db.QueryIntegerParameter;
import com.kit.lightserver.services.db.QueryParameter;
import com.kit.lightserver.services.db.QueryStringParameter;

final class InsertLogConexoesQuery implements InsertQueryInterface {

    static private final Logger LOGGER = LoggerFactory.getLogger(InsertLogConexoesQuery.class);

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    public InsertLogConexoesQuery(final InstallationIdSTY installationIdSTY, final String ktClientId, final int status,
            final ConnectionInfo connectionInfo) {

        final String idAStr = Long.toHexString(installationIdSTY.getInstallationId1());
        final String idBStr = Long.toHexString(installationIdSTY.getInstallationId2());
        final String idABStr = idAStr + ":" + idBStr;

        final String mobileNetworkAddress = connectionInfo.getClientHostAddress();
        final String properMobileNetworkAddress;
        if( mobileNetworkAddress.length() > 15 ) {
            LOGGER.error("mobileNetworkAddress is too long. mobileNetworkAddress="+mobileNetworkAddress);
            properMobileNetworkAddress = mobileNetworkAddress.substring(0, 15);
        }
        else {
            properMobileNetworkAddress = mobileNetworkAddress;
        }

        final String connectionUniqueId = connectionInfo.getConnectionUniqueId();

        final QueryStringParameter ktConexaoIdParam = new QueryStringParameter(connectionUniqueId);
        final QueryStringParameter idABParam = new QueryStringParameter(idABStr);
        final QueryStringParameter ktClientIdParam = new QueryStringParameter(ktClientId);
        final QueryIntegerParameter statusParam = new QueryIntegerParameter(status);
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
                "INSERT INTO " + TableLogConexoesConstants.TABLE_LOG_CONEXOES
                + " (KTConexaoDBTime, KTCelularIdAB, KTClientID, KTStatusDaConexao, KTConexaoID, KTCelularNetworkAddress) values (GETDATE(), ?, ?, ?, ?, ?)";

        return queryStr;

    }

    @Override
    public List<QueryParameter> getInsertQueryParameters() {
        return queryParameters;
    }

}// class
