package com.kit.lightserver.services.db.forms.notasfiscais;

import java.util.LinkedList;
import java.util.List;

import com.kit.lightserver.services.db.QueryIntegerParameter;
import com.kit.lightserver.services.db.QueryParameter;
import com.kit.lightserver.services.db.SelectQueryInterface;
import com.kit.lightserver.services.db.common.QueryUtil;

final public class SelectNotasfiscaisQuery implements SelectQueryInterface {

    private final String parentKnowledgeRowIdOrClause;

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    public SelectNotasfiscaisQuery(final List<Integer> parentKnowledgeRowIdList) {

        if (parentKnowledgeRowIdList.size() < 1) {
            throw new RuntimeException("The list can not be empty.");
        }

        final String orClause = QueryUtil.buildLongOrClause("knowledgeRowId", parentKnowledgeRowIdList.size());

        for (final Integer currentParentKnowledgeRowId : parentKnowledgeRowIdList) {
            final QueryIntegerParameter ktClientIdParam = new QueryIntegerParameter(currentParentKnowledgeRowId);
            queryParameters.add(ktClientIdParam);
        }

        this.parentKnowledgeRowIdOrClause = orClause;

    }// constructor

    @Override
    public String getPreparedSelectQueryString() {

        final String selectQueryStr = "SELECT KTRowId, KTStatus, knowledgeRowId, receiptNumber, receiptSerial, deliveryStatus, deliveryDate FROM "
                + TableNotasfiscaisConstants.TABLE_NAME_NOTASFISCAIS + " WHERE KTFlagHistorico=0 AND ( " + parentKnowledgeRowIdOrClause + " )";

        return selectQueryStr;

    }

    @Override
    public List<QueryParameter> getSelectQueryParameters() {
        return queryParameters;
    }

}// class
