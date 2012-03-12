package com.kit.lightserver.services.db.forms.conhecimentos.common;

import java.util.LinkedList;
import java.util.List;

import com.fap.framework.db.QueryIntParameter;
import com.fap.framework.db.QueryParameter;
import com.fap.framework.db.QueryStringParameter;
import com.fap.framework.db.SelectQueryInterface;
import com.kit.lightserver.domain.types.ConhecimentoIdSTY;
import com.kit.lightserver.services.db.forms.conhecimentos.TableConhecimentosConstants;

final class SelectConhecimentoEspecifico implements SelectQueryInterface {

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    public SelectConhecimentoEspecifico(final String ktClientId, final ConhecimentoIdSTY conhecimentoId) {

        final QueryStringParameter ktClientIdParam = new QueryStringParameter(ktClientId);
        queryParameters.add(ktClientIdParam);

        final int currentKtRowId = conhecimentoId.getKtRowId();
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
                " FROM " + TableConhecimentosConstants.TABLE_NAME_CONHECIMENTOS + "" +
                " WHERE KTClientId=? AND KTRowId=? AND KTFlagHistorico=0 AND KTControleProntoParaEnviar=1";

        return selectQueryStr;
    }

}// class
