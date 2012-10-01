package com.kit.lightserver.services.be.gps;

public final class DBGTables {

    static public class ACTIVITY_GPS_HISTORY {
        static public final String TABLE_NAME = "[dbo].[ActivityGpsHistory]";
        static public final String FORMID = "ID";
        static public final String PARENT_FORMID = "PID";
        static public final String FORM_CLIENT_ROWID = "KTRowId";
    }

    static public class ACTIVITY_GPS_LAST {
        static public final String TABLE_NAME = "[dbo].[ActivityGpsLast]";
    }

}// class
