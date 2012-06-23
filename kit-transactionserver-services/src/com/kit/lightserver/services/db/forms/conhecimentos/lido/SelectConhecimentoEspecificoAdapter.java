package com.kit.lightserver.services.db.forms.conhecimentos.lido;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.dajo.framework.db.SelectQueryResultAdapter;

import com.kit.lightserver.domain.types.KTFlagVO;

public final class SelectConhecimentoEspecificoAdapter implements SelectQueryResultAdapter<KTFlagVO> {

    @Override
    public KTFlagVO adaptResultSet(final ResultSet rs) throws SQLException {
        while (rs.next()) {
            final boolean flagLido = rs.getBoolean("KTFlagLido");
            final KTFlagVO flag = new KTFlagVO("KTFlagLido", flagLido);
            return flag;
        }
        return new KTFlagVO("KTFlagLido");
    }

}// class
