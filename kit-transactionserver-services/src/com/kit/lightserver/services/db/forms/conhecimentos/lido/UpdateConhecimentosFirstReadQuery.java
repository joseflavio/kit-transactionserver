package com.kit.lightserver.services.db.forms.conhecimentos.lido;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.dajo.framework.db.QueryDateParameter;
import org.dajo.framework.db.QueryIntParameter;
import org.dajo.framework.db.QueryParameter;
import org.dajo.framework.db.QueryStringParameter;
import org.dajo.framework.db.UpdateQueryInterface;

import com.kit.lightserver.domain.types.ConhecimentoIdSTY;
import com.kit.lightserver.services.db.forms.conhecimentos.TableConhecimentosConstants;

public final class UpdateConhecimentosFirstReadQuery implements UpdateQueryInterface {

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    public UpdateConhecimentosFirstReadQuery( final String ktClientId, final ConhecimentoIdSTY conhecimentoId, final Date dataDaLeitura) {

        final QueryDateParameter ktFirstReadDate = new QueryDateParameter(dataDaLeitura);
        queryParameters.add(ktFirstReadDate);

        final QueryStringParameter ktClientIdParam = new QueryStringParameter(ktClientId);
        queryParameters.add(ktClientIdParam);

        final int currentKtRowId = conhecimentoId.getKtRowId();
        final QueryIntParameter ktRowIdParam = new QueryIntParameter(currentKtRowId);
        queryParameters.add(ktRowIdParam);

    }// constructor

    @Override
    public String getPreparedUpdateQueryString() {

        final String queryStr =
                "UPDATE " + TableConhecimentosConstants.TABLE_NAME_CONHECIMENTOS +
                " SET KTCelularDataPrimeiraLeitura=?, KTFlagLido=1, KTFlagLidoUpdateDBTime=GETDATE(), KTFormVersion=KTFormVersion+1" +
                " WHERE KTClientId=? AND KTRowId=? AND KTFlagLido=0";

        return queryStr;

    }

    @Override
    public List<QueryParameter> getUpdateQueryParameters() {
        return queryParameters;
    }

}// class
