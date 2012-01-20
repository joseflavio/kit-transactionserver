package com.kit.lightserver.services.db.authenticate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.kit.lightserver.services.db.SelectQueryResultAdapter;

public final class SelectLoggedInClientIdsResultAdapter implements SelectQueryResultAdapter<List<String>> {

    @Override
    public List<String> adaptResultSet(final ResultSet rs) throws SQLException {
        List<String> result = new LinkedList<String>();
        while( rs.next() ) {
            String ktClientId = rs.getString("KTClientId");
            result.add(ktClientId);
        }
        return result;
    }

}// class
