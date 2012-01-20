package com.kit.lightserver.services.db.authenticate;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.kit.lightserver.services.be.authentication.AuthenticateQueryResult;
import com.kit.lightserver.services.db.SelectQueryResultAdapter;

final class SelectClientIdAndPasswordResultAdapter implements SelectQueryResultAdapter<AuthenticateQueryResult> {

    @Override
    public AuthenticateQueryResult adaptResultSet(final ResultSet rs) throws SQLException {
        final AuthenticateQueryResult result;
        if (rs.next()) {
            String ktClientId = rs.getString("KTClientId");
            String ktPassword = rs.getString("KTPassword");
            boolean ktDeveResetar = rs.getBoolean("KTDeveResetar");
            boolean ktUsuarioConectado = rs.getBoolean("KTUsuarioConectado");
            result = new AuthenticateQueryResult(ktClientId, ktPassword, ktDeveResetar, ktUsuarioConectado);
        } else {
            // User do not exist
            result = new AuthenticateQueryResult();
        }
        return result;
    }

}// class
