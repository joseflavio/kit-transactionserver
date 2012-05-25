package com.kit.lightserver.services.db.authenticate;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.fap.framework.db.SelectQueryResultAdapter;

public final class SelectAuthenticateLastSuccessResultAdapter implements SelectQueryResultAdapter<SelectAuthenticateLastSuccessResult> {

    @Override
    public SelectAuthenticateLastSuccessResult adaptResultSet(final ResultSet rs) throws SQLException {
        if( rs.next() == true ) {
            final String lastInstallationIdAb = rs.getString("KTCelularIdAB");
            final int lastVersion = rs.getInt("KTVersao");
            final String lastConnectionUniqueId = rs.getString("KTConexaoID");
            final SelectAuthenticateLastSuccessResult result = new SelectAuthenticateLastSuccessResult(lastInstallationIdAb, lastConnectionUniqueId, lastVersion);
            return result;
        }
        return new SelectAuthenticateLastSuccessResult();
    }

}// class
