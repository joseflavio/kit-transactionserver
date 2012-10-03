package com.kit.lightserver.services.be.gps;

import java.util.LinkedList;
import java.util.List;

import org.dajo.framework.db.QueryBooleanParameter;
import org.dajo.framework.db.QueryDateParameter;
import org.dajo.framework.db.QueryIntParameter;
import org.dajo.framework.db.QueryParameter;
import org.dajo.framework.db.QueryStringParameter;
import org.dajo.framework.db.UpdateQueryInterface;
import org.dajo.math.IntegerUtils;

import com.kit.lightserver.domain.types.ConnectionInfoVO;
import com.kit.lightserver.domain.types.CoordenadaGpsSTY;
import com.kit.lightserver.domain.types.InstallationIdAbVO;

class UpdateActivityGpsLastQuery implements UpdateQueryInterface {

    private final List<QueryParameter> queryParameters = new LinkedList<>();

    UpdateActivityGpsLastQuery(final InstallationIdAbVO installationId, final String clientUserId, final ConnectionInfoVO connectionId,
            final boolean gpsAvailable, final CoordenadaGpsSTY mostRecent) {

        /*
         * Set Parameters
         */
        int lat = IntegerUtils.checkedLongToInt( Math.round( ((double)mostRecent.getLatitude()) * 10000000) );
        int lng = IntegerUtils.checkedLongToInt( Math.round( ((double)mostRecent.getLongitude()) * 10000000) );
        int accuracy = IntegerUtils.checkedLongToInt( Math.round( ((double)mostRecent.getAccuracy()) * 10000000) );
        int logicalClock = IntegerUtils.checkedLongToInt( mostRecent.getLogicalClock() );

        final QueryStringParameter connectionIdParam = new QueryStringParameter( connectionId.getConnectionUniqueId() );
        final QueryBooleanParameter gpsAvailableParam = new QueryBooleanParameter( Boolean.valueOf(gpsAvailable) );

        final QueryIntParameter  latParam = new QueryIntParameter( lat );
        final QueryIntParameter  lngParam = new QueryIntParameter( lng );
        final QueryIntParameter  accuracyParam = new QueryIntParameter( accuracy );

        final QueryIntParameter  logicalClockParam = new QueryIntParameter( logicalClock );
        final QueryDateParameter activityTime = new QueryDateParameter( mostRecent.getTime() );

        queryParameters.add(connectionIdParam);
        queryParameters.add(gpsAvailableParam);
        queryParameters.add(latParam);
        queryParameters.add(lngParam);
        queryParameters.add(accuracyParam);
        queryParameters.add(logicalClockParam);
        queryParameters.add(activityTime);

        /*
         * Where parameters
         */
        final QueryStringParameter installationIdParam = new QueryStringParameter( installationId.getIdABStr() );
        final QueryStringParameter clientIdParam = new QueryStringParameter( clientUserId.toUpperCase() );

        queryParameters.add(installationIdParam);
        queryParameters.add(clientIdParam);

    }

    @Override
    public String getPreparedUpdateQueryString() {
        final String insertQueryStr =
                "UPDATE " + DBGTables.ACTIVITY_GPS_LAST.TABLE_NAME + " SET " +
                "[KTConnectionId]=?, [KTAvailable]=?, [KTLat]=?, [KTLng]=?, [KTAccuracy]=?, [KTActivityLogicalClock]=?, " +
                "[KTActivityTime]=?, [KTLastUpdateDBTime]="+MsSql.SYSUTCDATETIME+" WHERE [KTClientInstallIdAB]=? AND [KTClientUserId]=?";

        return insertQueryStr;
    }

    @Override
    public List<QueryParameter> getUpdateQueryParameters() {
        return queryParameters;
    }

}
