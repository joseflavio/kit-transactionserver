package com.kit.lightserver.services.db.authenticate;

import java.util.LinkedList;
import java.util.List;

import org.dajo.framework.db.InsertQueryInterface;
import org.dajo.framework.db.MsSql;
import org.dajo.framework.db.QueryParameter;
import org.dajo.framework.db.QueryStringParameter;

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
                " ([KTClientUserId], [KTClientInstallIdAB], [KTConnectionId], [KTVersion], [KTUpdateDBTime]) VALUES ( ?, ?, ?, 1, "+MsSql.SYSUTCDATETIME+" )";

        return selectQueryStr;

    }

    @Override
    public List<QueryParameter> getInsertQueryParameters() {
        return queryParameters;
    }

}// class
