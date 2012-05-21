package com.kit.lightserver.services.db.authenticate;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.fap.framework.db.SelectQueryResultAdapter;
import com.kit.lightserver.services.be.authentication.AuthenticateQueryResult;

final class SelectClientIdAndPasswordResultAdapter implements SelectQueryResultAdapter<AuthenticateQueryResult> {

    @Override
    public AuthenticateQueryResult adaptResultSet(final ResultSet rs) throws SQLException {
        final AuthenticateQueryResult result;
        if (rs.next()) {
            String ktClientId = rs.getString("KTClientId");
            String ktPassword = rs.getString("KTPassword");
            result = new AuthenticateQueryResult(ktClientId, ktPassword);
        } else {
            // User do not exist
            result = new AuthenticateQueryResult();
        }
        return result;
    }

}// class
