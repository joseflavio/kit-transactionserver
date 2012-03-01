package com.kit.lightserver.services.db.forms.conhecimentos;

import java.util.LinkedList;
import java.util.List;

import com.fap.framework.db.QueryParameter;
import com.fap.framework.db.SelectQueryInterface;
import com.kit.lightserver.services.db.QueryStringParameter;

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
                "KTRowId, KTClientId, KTFlagRecebido, KTFlagLido, KTFlagEditado, KTFieldNumeroDoConhecimento, KTFieldSerialDoConhecimento, " +
                "KTFieldCodigoDaSubsidiaria, KTFieldRemetenteId, KTFieldNomeDoDestinatario, KTCelularEntregaStatus " +
                "FROM " + TableConhecimentosConstants.TABLE_NAME_CONHECIMENTOS + " WHERE KTClientId=? AND KTFlagHistorico=0 AND KTControleProntoParaEnviar=1";

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
