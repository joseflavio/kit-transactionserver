package com.kit.lightserver.services.db.dbd;

import java.util.LinkedList;
import java.util.List;

import org.dajo.framework.db.QueryParameter;
import org.dajo.framework.db.QueryStringParameter;
import org.dajo.framework.db.SelectQueryInterface;
import org.dajo.framework.db.util.QueryUtil;

import com.kit.lightserver.domain.types.FormUniqueIdSTY;

final public class SelectNotasfiscaisQuery implements SelectQueryInterface {

    private final String parentKnowledgeRowIdOrClause;

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    private final boolean selecionarSomenteNaoRecebidos;

    public SelectNotasfiscaisQuery(final String ktClientUserId, final List<FormUniqueIdSTY> parentFormsIds, final boolean selecionarSomenteNaoRecebidos) {

        /*
         * Sanity
         */
        if (parentFormsIds.size() < 1) {
            throw new RuntimeException("The list can not be empty.");
        }

        this.selecionarSomenteNaoRecebidos = selecionarSomenteNaoRecebidos;

        queryParameters.add( new QueryStringParameter(ktClientUserId) );

        final String orClause = QueryUtil.buildLongOrClause("[KTParentConhecimentoRowId]", parentFormsIds.size());

        for (final FormUniqueIdSTY parent : parentFormsIds) {
            assert( FormUniqueIdSTY.isConhecimento(parent) == true );
            final QueryStringParameter parentParam = new QueryStringParameter( parent.getFormId().getValue() );
            queryParameters.add(parentParam);
        }

        this.parentKnowledgeRowIdOrClause = orClause;

    }// constructor

    @Override
    public String getPreparedSelectQueryString() {

        String selectQueryStr = "SELECT KTRowId, KTParentConhecimentoRowId, KTFieldReceiptNumber, KTFieldReceiptSerial, KTCelularEntregaStatus, " +
        		"KTCelularEntregaData FROM " + DBDTables.NOTASFISCAIS.TABLE_NAME +
        		" WHERE KTClientUserId=? AND KTFlagRemovido=0 AND ( " + parentKnowledgeRowIdOrClause + " )";

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
