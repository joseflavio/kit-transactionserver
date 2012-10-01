package com.kit.lightserver.services.be.gps;

import java.util.List;

import org.dajo.framework.db.InsertQueryInterface;
import org.dajo.framework.db.QueryParameter;

public class InsertActivityGpsLastQuery implements InsertQueryInterface {

    @Override
    public List<QueryParameter> getInsertQueryParameters() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getPreparedInsertQueryString() {

        final String insertQueryStr =
                "INSERT INTO " + DBGTables.ACTIVITY_GPS_LAST.TABLE_NAME + " ( " +
                "[KTClientInstallIdAB], [KTClientUserId], [KTConnectionId], [KTAvailable], " +
                "[KTLat], [KTLng], [KTAccuracy], [KTActivityLogicalClock], [KTActivityTime], [KTLastUpdateDBTime]) VALUES " +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, "+MsSql.SYSUTCDATETIME+")";

        return insertQueryStr;
    }



}// class
