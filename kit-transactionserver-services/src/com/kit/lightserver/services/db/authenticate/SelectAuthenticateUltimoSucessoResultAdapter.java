package com.kit.lightserver.services.db.authenticate;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.fap.framework.db.SelectQueryResultAdapter;

public final class SelectAuthenticateUltimoSucessoResultAdapter implements SelectQueryResultAdapter<SelectAuthenticateUltimoSucessoResult> {

    @Override
    public SelectAuthenticateUltimoSucessoResult adaptResultSet(final ResultSet rs) throws SQLException {
        if( rs.next() == true ) {
            final String lastInstallationIdAb = rs.getString("KTClientInstallIdAB");
            final int lastVersion = rs.getInt("KTVersion");
            final String lastConnectionUniqueId = rs.getString("KTConnectionId");
            final SelectAuthenticateUltimoSucessoResult result = new SelectAuthenticateUltimoSucessoResult(lastInstallationIdAb, lastConnectionUniqueId, lastVersion);
            return result;
        }
        return new SelectAuthenticateUltimoSucessoResult();
    }

}// class
