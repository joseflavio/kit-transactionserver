package com.kit.lightserver.services.db.forms.conhecimentos;

import java.util.LinkedList;
import java.util.List;

import com.kit.lightserver.services.db.QueryParameter;
import com.kit.lightserver.services.db.QueryStringParameter;
import com.kit.lightserver.services.db.SelectQueryInterface;

final public class SelectConhecimentosQuery implements SelectQueryInterface {

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();
    private final boolean selecionarSomenteNaoRecebidos;

    public SelectConhecimentosQuery(final String ktClientId, final boolean selecionarSomenteNaoRecebidos) {

        final QueryStringParameter ktClientIdParam = new QueryStringParameter(ktClientId);
        queryParameters.add(ktClientIdParam);

        this.selecionarSomenteNaoRecebidos = selecionarSomenteNaoRecebidos;

    }// constructor

    @Override
    public String getPreparedSelectQueryString() {

        String selectQueryStr = "SELECT KTRowId, KTClientId, KTStatus, KTFlagRecebido, KTFlagLido, KTFlagEditado, KTFieldNumeroDoConhecimento, knowledgeSerial, subsidiaryCode, senderId, recipientName, deliveryStatus, "
                + "deliveryDate FROM " + TableConhecimentosConstants.TABLE_NAME_CONHECIMENTOS + " WHERE KTClientId=? AND KTFlagHistorico=0 AND KTControleProntoParaEnviar=1";

        if( selecionarSomenteNaoRecebidos == true ) {
            selectQueryStr += " AND KTFlagRecebido=0";
        }

        return selectQueryStr;

    }

    @Override
    public List<QueryParameter> getSelectQueryParameters() {
        return queryParameters;
    }

}// class
