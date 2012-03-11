package com.kit.lightserver.services.db.forms.conhecimentos.lido;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.fap.framework.db.SelectQueryResultAdapter;

public final class SelectConhecimentoEspecificoAdapter implements SelectQueryResultAdapter<Boolean> {

    @Override
    public Boolean adaptResultSet(final ResultSet rs) throws SQLException {
        while (rs.next()) {
            final boolean flagLido = rs.getBoolean("KTFlagLido");
            return Boolean.valueOf(flagLido);
        }
        return null;
    }

}// class
