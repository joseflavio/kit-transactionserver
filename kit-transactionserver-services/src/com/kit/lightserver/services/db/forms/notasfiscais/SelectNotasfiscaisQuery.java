package com.kit.lightserver.services.db.forms.notasfiscais;

import java.util.LinkedList;
import java.util.List;

import org.dajo.framework.db.QueryIntParameter;
import org.dajo.framework.db.QueryParameter;
import org.dajo.framework.db.QueryStringParameter;
import org.dajo.framework.db.SelectQueryInterface;
import org.dajo.framework.db.util.QueryUtil;

import com.kit.lightserver.domain.types.FormConhecimentoRowIdSTY;

final public class SelectNotasfiscaisQuery implements SelectQueryInterface {

    private final String parentKnowledgeRowIdOrClause;

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    private final boolean selecionarSomenteNaoRecebidos;

    public SelectNotasfiscaisQuery(final String ktClientUserId, final List<FormConhecimentoRowIdSTY> parentRowIds, final boolean selecionarSomenteNaoRecebidos) {

        /*
         * Sanity
         */
        if (parentRowIds.size() < 1) {
            throw new RuntimeException("The list can not be empty.");
        }

        this.selecionarSomenteNaoRecebidos = selecionarSomenteNaoRecebidos;

        queryParameters.add( new QueryStringParameter(ktClientUserId) );

        final String orClause = QueryUtil.buildLongOrClause("[KTParentConhecimentoRowId]", parentRowIds.size());

        for (final FormConhecimentoRowIdSTY currentParentKnowledgeRowId : parentRowIds) {
            final QueryIntParameter ktClientIdParam = new QueryIntParameter( currentParentKnowledgeRowId.getKtFormRowId() );
            queryParameters.add(ktClientIdParam);
        }

        this.parentKnowledgeRowIdOrClause = orClause;

    }// constructor

    @Override
    public String getPreparedSelectQueryString() {

        String selectQueryStr = "SELECT KTRowId, KTParentConhecimentoRowId, KTFieldReceiptNumber, KTFieldReceiptSerial, KTCelularEntregaStatus, KTCelularEntregaData FROM "
                + TableNotasfiscaisConstants.TABLE_NAME_NOTASFISCAIS + " WHERE KTClientUserId=? AND KTFlagHistorico=0 AND ( " + parentKnowledgeRowIdOrClause + " )";

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
