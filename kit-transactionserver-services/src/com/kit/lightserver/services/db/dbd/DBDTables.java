package com.kit.lightserver.services.db.dbd;

public final class DBDTables {

    static public class CONHECIMENTOS {
        static public final String TABLE_NAME = "[dbo].[FormConhecimentos]";
    }

    static public class NOTASFISCAIS {
        static public final String TABLE_NAME = "[dbo].[FormNotasfiscais]";
        static public final String KTROWID = "KTRowId";
        static public final String FORMID = "ID";
        static public final String PARENT_FORMID = "OID";
    }

}// class
