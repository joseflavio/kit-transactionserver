package com.fap.framework.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.fap.framework.db.util.MsSqlBitConverter;

public final class SelectQueryResultAdapterBoolean implements SelectQueryResultAdapter<SelectQueryResultSingleBoolean> {

    @Override
    public SelectQueryResultSingleBoolean adaptResultSet(final ResultSet rs) throws SQLException {
        if( rs.next() ) {
            boolean result = MsSqlBitConverter.convert(rs.getInt(1));
            return new SelectQueryResultSingleBoolean(result);
        }
        return new SelectQueryResultSingleBoolean();
    }

}// class
