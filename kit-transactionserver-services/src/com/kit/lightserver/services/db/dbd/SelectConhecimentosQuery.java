package com.kit.lightserver.services.db.dbd;

import java.util.LinkedList;
import java.util.List;

import org.dajo.framework.db.QueryParameter;
import org.dajo.framework.db.QueryStringParameter;
import org.dajo.framework.db.SelectQueryInterface;


final public class SelectConhecimentosQuery implements SelectQueryInterface {

    static private final int MAX_RETRIEVE_CONHECIMENTOS = 250;

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();
    private final boolean selecionarSomenteNaoRecebidos;

    public SelectConhecimentosQuery(final String ktClientId, final boolean selecionarSomenteNaoRecebidos) {

        final QueryStringParameter ktClientIdParam = new QueryStringParameter(ktClientId);
        queryParameters.add(ktClientIdParam);

        this.selecionarSomenteNaoRecebidos = selecionarSomenteNaoRecebidos;

    }// constructor

    @Override
    public String getPreparedSelectQueryString() {

        String selectQueryStr =
                "SELECT TOP " + MAX_RETRIEVE_CONHECIMENTOS + " " +
                "KTRowId, KTClientUserId, KTFlagRecebido, KTFlagLido, KTFlagEditado, KTFieldNumeroDoConhecimento, KTFieldSerialDoConhecimento, " +
                "KTFieldCodigoDaSubsidiaria, KTFieldRemetenteId, KTFieldNomeDoDestinatario, KTCelularEntregaStatus, KTLastUpdateDBTime, KTRowVersion" +
                " FROM " + DBDTables.TABLE_NAME_CONHECIMENTOS +
                " WHERE KTClientUserId=? AND KTFlagHistorico=0 AND KTControleProntoParaEnviar=1";

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
