package com.kit.lightserver.services.be.gps;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.dajo.framework.db.SelectQueryResultAdapter;
import org.dajo.framework.db.SelectQuerySingleResult;

public class SelectQueryResultAdapterInteger implements SelectQueryResultAdapter<SelectQuerySingleResult<Integer>> {

    @Override
    public SelectQuerySingleResult<Integer> adaptResultSet(final ResultSet rs) throws SQLException {
        if( rs.next() ) {
            final int result = rs.getInt(1);
            return new SelectQuerySingleResult<Integer>(Integer.valueOf(result));
        }
        return new SelectQuerySingleResult<Integer>();
    }

}
