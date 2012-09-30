package com.kit.lightserver.services.be.gps;

import java.util.List;

import org.dajo.framework.db.BatchInsertQueryInterface;
import org.dajo.framework.db.BatchInsertQueryParameters;

final class BatchInsertActivityGpsHistoryQuery implements BatchInsertQueryInterface {

    private final List<BatchInsertQueryParameters> paramsList;

    public BatchInsertActivityGpsHistoryQuery(final List<BatchInsertQueryParameters> paramsList) {
        this.paramsList = paramsList;
    }

    @Override
    public String getPreparedInsertQueryString() {

        final String insertQueryStr =
                "INSERT INTO " + DBGTables.ACTIVITY_GPS_HISTORY.TABLE_NAME + " ( " +
                "[KTClientInstallIdAB], [KTClientUserId], [KTConnectionId], [KTAvailable], " +
                "[KTLat], [KTLng], [KTAccuracy], [KTActivityLogicalClock], [KTActivityTime] ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        return insertQueryStr;

    }

    @Override
    public List<BatchInsertQueryParameters> getInsertQueryParametersList() {
        return paramsList;
    }

}// class
