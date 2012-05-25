package com.kit.lightserver.services.db.authenticate;

import java.util.LinkedList;
import java.util.List;

import com.fap.framework.db.InsertQueryInterface;
import com.fap.framework.db.QueryParameter;
import com.fap.framework.db.QueryStringParameter;
import com.kit.lightserver.domain.types.ConnectionInfoVO;
import com.kit.lightserver.domain.types.InstallationIdAbVO;

final class InsertAuthenticateUltimaConexaoQuery implements InsertQueryInterface {

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    InsertAuthenticateUltimaConexaoQuery(final String ktClientId, final InstallationIdAbVO installationIdSTY, final ConnectionInfoVO connectionInfo) {

        final QueryStringParameter ktClientIdParam = new QueryStringParameter( ktClientId.toUpperCase() );
        final QueryStringParameter installationIdAbParam = new QueryStringParameter( installationIdSTY.getIdABStr() );
        final QueryStringParameter connectionUniqueIdParam = new QueryStringParameter( connectionInfo.getConnectionUniqueId() );

        queryParameters.add(ktClientIdParam);
        queryParameters.add(installationIdAbParam);
        queryParameters.add(connectionUniqueIdParam);

    }// constructor

    @Override
    public String getPreparedInsertQueryString() {

        final String selectQueryStr =
                "INSERT INTO " + TableAuthenticateConstants.TABLE_AUTHENTICATE_ULTIMACONEXAO +
                " ([KTLastUpdateDBTime], [KTClientId], [KTCelularIdAB], [KTConexaoID], [KTVersao]) VALUES (GETDATE(), ?, ?, ?, 1)";

        return selectQueryStr;

    }

    @Override
    public List<QueryParameter> getInsertQueryParameters() {
        return queryParameters;
    }

}// class
