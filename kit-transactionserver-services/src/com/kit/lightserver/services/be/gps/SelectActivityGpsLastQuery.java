package com.kit.lightserver.services.be.gps;

import java.util.LinkedList;
import java.util.List;

import org.dajo.framework.db.QueryParameter;
import org.dajo.framework.db.QueryStringParameter;
import org.dajo.framework.db.SelectQueryInterface;

import com.kit.lightserver.domain.types.InstallationIdAbVO;

public class SelectActivityGpsLastQuery implements SelectQueryInterface {

    private final List<QueryParameter> queryParameters = new LinkedList<>();

    public SelectActivityGpsLastQuery(final InstallationIdAbVO installationId, final String clientUserId) {
        final QueryStringParameter installationIdParam = new QueryStringParameter( installationId.getIdABStr() );
        final QueryStringParameter clientIdParam = new QueryStringParameter( clientUserId.toUpperCase() );
        queryParameters.add(installationIdParam);
        queryParameters.add(clientIdParam);
    }

    @Override
    public String getPreparedSelectQueryString() {
        final String selectQueryStr =
                "SELECT [KTActivityLogicalClock] FROM " + DBGTables.ACTIVITY_GPS_LAST.TABLE_NAME + " WHERE [KTClientInstallIdAB]=? AND [KTClientUserId]=?";
        return selectQueryStr;
    }

    @Override
    public List<QueryParameter> getSelectQueryParameters() {
        return queryParameters;
    }

}
