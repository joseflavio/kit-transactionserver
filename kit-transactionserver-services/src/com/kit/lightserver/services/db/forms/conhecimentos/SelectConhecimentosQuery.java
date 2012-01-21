package com.kit.lightserver.services.db.forms.conhecimentos;

import java.util.LinkedList;
import java.util.List;

import com.kit.lightserver.services.db.QueryParameter;
import com.kit.lightserver.services.db.QueryStringParameter;
import com.kit.lightserver.services.db.SelectQueryInterface;

final public class SelectConhecimentosQuery implements SelectQueryInterface {

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    public SelectConhecimentosQuery(final String ktClientId) {

        final QueryStringParameter ktClientIdParam = new QueryStringParameter(ktClientId);
        queryParameters.add(ktClientIdParam);

    }// constructor

    @Override
    public String getPreparedSelectQueryString() {

        final String selectQueryStr = "SELECT KTRowId, KTClientId, KTStatus, KTFlagRecebido, KTFlagLido, KTFlagEditado, knowledgeNumber, knowledgeSerial, subsidiaryCode, senderId, recipientName, deliveryStatus, "
                + "deliveryDate FROM " + TableConhecimentosConstants.TABLE_NAME_CONHECIMENTOS + " WHERE KTClientId=? and KTFlagHistorico=0 and KTControleProntoParaEnviar=1";

        return selectQueryStr;

    }

    @Override
    public List<QueryParameter> getSelectQueryParameters() {
        return queryParameters;
    }

}// class
