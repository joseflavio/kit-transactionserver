package com.kit.lightserver.services.be.gps;

import java.util.LinkedList;
import java.util.List;

import org.dajo.framework.db.InsertQueryInterface;
import org.dajo.framework.db.QueryBooleanParameter;
import org.dajo.framework.db.QueryDateParameter;
import org.dajo.framework.db.QueryIntParameter;
import org.dajo.framework.db.QueryParameter;
import org.dajo.framework.db.QueryStringParameter;
import org.dajo.math.IntegerUtils;

import com.kit.lightserver.domain.types.ConnectionInfoVO;
import com.kit.lightserver.domain.types.CoordenadaGpsSTY;
import com.kit.lightserver.domain.types.InstallationIdAbVO;

final class InsertActivityGpsHistoryQuery implements InsertQueryInterface {

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    InsertActivityGpsHistoryQuery(final InstallationIdAbVO installationId, final String clientUserId, final ConnectionInfoVO connectionId,
            final boolean available, final CoordenadaGpsSTY coordenadaGps) {

        int lat = IntegerUtils.checkedLongToInt( Math.round( ((double)coordenadaGps.getLatitude()) * 10000000) );
        int lng = IntegerUtils.checkedLongToInt( Math.round( ((double)coordenadaGps.getLongitude()) * 10000000) );
        int accuracy = IntegerUtils.checkedLongToInt( Math.round( ((double)coordenadaGps.getAccuracy()) * 10000000) );
        int logicalClock = IntegerUtils.checkedLongToInt( coordenadaGps.getLogicalClock() );

        final QueryStringParameter idABParam = new QueryStringParameter( installationId.getIdABStr() );
        final QueryStringParameter clientIdParam = new QueryStringParameter( clientUserId.toUpperCase() );
        final QueryStringParameter connectionIdParam = new QueryStringParameter( connectionId.getConnectionUniqueId() );
        final QueryBooleanParameter gpsAvailableParam = new QueryBooleanParameter( Boolean.valueOf(available) );

        final QueryIntParameter  latParam = new QueryIntParameter( lat );
        final QueryIntParameter  lngParam = new QueryIntParameter( lng );
        final QueryIntParameter  accuracyParam = new QueryIntParameter( accuracy );

        final QueryIntParameter  logicalClockParam = new QueryIntParameter( logicalClock );
        final QueryDateParameter activityTime = new QueryDateParameter( coordenadaGps.getTime() );

        queryParameters.add(idABParam);
        queryParameters.add(clientIdParam);
        queryParameters.add(connectionIdParam);
        queryParameters.add(gpsAvailableParam);
        queryParameters.add(latParam);
        queryParameters.add(lngParam);
        queryParameters.add(accuracyParam);
        queryParameters.add(logicalClockParam);
        queryParameters.add(activityTime);


    }// constructor

    @Override
    public String getPreparedInsertQueryString() {

        final String insertQueryStr =
                "INSERT INTO " + DBGTables.ACTIVITY_GPS_HISTORY.TABLE_NAME + " ( " +
                "[KTClientInstallIdAB], [KTClientUserId], [KTConnectionId], [KTAvailable], " +
                "[KTLat], [KTLng], [KTAccuracy], [KTActivityLogicalClock], [KTActivityTime] ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        return insertQueryStr;

    }

    @Override
    public List<QueryParameter> getInsertQueryParameters() {
        return queryParameters;
    }

}// class
