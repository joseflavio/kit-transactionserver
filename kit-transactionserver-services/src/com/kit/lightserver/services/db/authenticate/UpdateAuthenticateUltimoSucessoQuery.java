package com.kit.lightserver.services.db.authenticate;

import java.util.LinkedList;
import java.util.List;

import org.dajo.framework.db.QueryIntegerParameter;
import org.dajo.framework.db.QueryParameter;
import org.dajo.framework.db.QueryStringParameter;
import org.dajo.framework.db.UpdateQueryInterface;

import com.kit.lightserver.domain.types.InstallationIdAbVO;

final class UpdateAuthenticateUltimoSucessoQuery implements UpdateQueryInterface {

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    UpdateAuthenticateUltimoSucessoQuery(
            final InstallationIdAbVO newInstallationIdAb, final String newConnectionUniqueId,
            final String clientUserId, final InstallationIdAbVO lastInstallationIdAb, final String lastConnectionUniqueId, final int lastVersion) {

        final QueryStringParameter newInstallIdAbParam = new QueryStringParameter( newInstallationIdAb.getIdABStr() );
        final QueryStringParameter newConnectionUniqueIdParam = new QueryStringParameter( newConnectionUniqueId );

        queryParameters.add(newInstallIdAbParam);
        queryParameters.add(newConnectionUniqueIdParam);

        final QueryStringParameter clientUserIdParam = new QueryStringParameter( clientUserId.toUpperCase() );
        final QueryStringParameter lastInstallIdAbParam = new QueryStringParameter( lastInstallationIdAb.getIdABStr() );
        final QueryStringParameter lastConnectionUniqueIdParam = new QueryStringParameter( lastConnectionUniqueId );
        final QueryIntegerParameter lastVersionParam = new QueryIntegerParameter(Integer.valueOf(lastVersion));


        queryParameters.add(clientUserIdParam);
        queryParameters.add(lastInstallIdAbParam);
        queryParameters.add(lastConnectionUniqueIdParam);
        queryParameters.add(lastVersionParam);

    }// constructor

    @Override
    public String getPreparedUpdateQueryString() {

        final String queryStr =
                "UPDATE " + TableAuthenticateConstants.TABLE_AUTHENTICATE_ULTIMACONEXAO + " SET " +
                " [KTUpdateDBTime]=SYSUTCDATETIME()," +
                " [KTClientInstallIdAB]=?," +
                " [KTConnectionId]=?," +
                " [KTVersion]=([KTVersion]+1)" +
                " WHERE [KTClientUserId]=? AND [KTClientInstallIdAB]=? AND [KTConnectionId]=? AND [KTVersion]=?";

        return queryStr;

    }

    @Override
    public List<QueryParameter> getUpdateQueryParameters() {
        return queryParameters;
    }

}// class
