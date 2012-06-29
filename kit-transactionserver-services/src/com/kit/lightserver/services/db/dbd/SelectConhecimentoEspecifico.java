package com.kit.lightserver.services.db.dbd;

import java.util.LinkedList;
import java.util.List;

import org.dajo.framework.db.QueryIntParameter;
import org.dajo.framework.db.QueryParameter;
import org.dajo.framework.db.QueryStringParameter;
import org.dajo.framework.db.SelectQueryInterface;

import com.kit.lightserver.domain.types.FormConhecimentoRowIdSTY;

final class SelectConhecimentoEspecifico implements SelectQueryInterface {

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    public SelectConhecimentoEspecifico(final String ktClientId, final FormConhecimentoRowIdSTY conhecimentoId) {

        final QueryStringParameter ktClientIdParam = new QueryStringParameter(ktClientId);
        queryParameters.add(ktClientIdParam);

        final int currentKtRowId = conhecimentoId.getKtFormRowId();
        final QueryIntParameter ktRowIdParam = new QueryIntParameter(currentKtRowId);
        queryParameters.add(ktRowIdParam);

    }

    @Override
    public List<QueryParameter> getSelectQueryParameters() {
        return queryParameters;
    }

    @Override
    public String getPreparedSelectQueryString() {

        String selectQueryStr =
                "SELECT KTFlagLido, KTCelularDataPrimeiraLeitura" +
                " FROM " + DBDTables.TABLE_NAME_CONHECIMENTOS + "" +
                " WHERE KTClientId=? AND KTRowId=? AND KTFlagHistorico=0 AND KTControleProntoParaEnviar=1";

        return selectQueryStr;
    }

}// class