package com.kit.lightserver.services.db.forms.notasfiscais;

import java.util.LinkedList;
import java.util.List;

import com.fap.framework.db.QueryParameter;
import com.fap.framework.db.SelectQueryInterface;
import com.kit.lightserver.services.db.QueryIntegerParameter;
import com.kit.lightserver.services.db.common.QueryUtil;

final public class SelectNotasfiscaisQuery implements SelectQueryInterface {

    private final String parentKnowledgeRowIdOrClause;

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    private final boolean selecionarSomenteNaoRecebidos;

    public SelectNotasfiscaisQuery(final List<Integer> parentKnowledgeRowIdList, final boolean selecionarSomenteNaoRecebidos) {

        this.selecionarSomenteNaoRecebidos = selecionarSomenteNaoRecebidos;

        if (parentKnowledgeRowIdList.size() < 1) {
            throw new RuntimeException("The list can not be empty.");
        }

        final String orClause = QueryUtil.buildLongOrClause("KTParentRowId", parentKnowledgeRowIdList.size());

        for (final Integer currentParentKnowledgeRowId : parentKnowledgeRowIdList) {
            final QueryIntegerParameter ktClientIdParam = new QueryIntegerParameter(currentParentKnowledgeRowId);
            queryParameters.add(ktClientIdParam);
        }

        this.parentKnowledgeRowIdOrClause = orClause;

    }// constructor

    @Override
    public String getPreparedSelectQueryString() {

        String selectQueryStr = "SELECT KTRowId, KTStatus, KTParentRowId, receiptNumber, receiptSerial, deliveryStatus, deliveryDate FROM "
                + TableNotasfiscaisConstants.TABLE_NAME_NOTASFISCAIS + " WHERE KTFlagHistorico=0 AND ( " + parentKnowledgeRowIdOrClause + " )";

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
