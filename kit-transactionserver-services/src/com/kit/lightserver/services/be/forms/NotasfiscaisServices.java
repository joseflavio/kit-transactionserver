package com.kit.lightserver.services.be.forms;

import java.util.LinkedList;
import java.util.List;

import com.kit.lightserver.domain.containers.SimpleServiceResponse;
import com.kit.lightserver.domain.types.ConhecimentoSTY;
import com.kit.lightserver.domain.types.NotafiscalSTY;
import com.kit.lightserver.services.db.SelectQueryResult;
import com.kit.lightserver.services.db.SelectQueryExecuter;
import com.kit.lightserver.services.db.forms.notasfiscais.SelectNotasfiscaisQuery;
import com.kit.lightserver.services.db.forms.notasfiscais.SelectNotasfiscaisQueryResultAdapter;

final class NotasfiscaisServices {

    static SimpleServiceResponse<List<NotafiscalSTY>> retrieveNotasfiscais(final List<ConhecimentoSTY> conhecimentoList,
            final boolean retrieveSomenteNaoRecebidos) {

        /*
         * Conhecimentos
         */
        final List<Integer> parentKnowledgeRowIdList = new LinkedList<Integer>();
        for (ConhecimentoSTY conhecimentoSTY : conhecimentoList) {
            parentKnowledgeRowIdList.add(conhecimentoSTY.getKtRowId());
        }

        final SelectNotasfiscaisQueryResultAdapter notasfiscaisAdapter = new SelectNotasfiscaisQueryResultAdapter();
        final SelectQueryExecuter<List<NotafiscalSTY>> notasfiscaisQueryExecuter = new SelectQueryExecuter<List<NotafiscalSTY>>(notasfiscaisAdapter);

        /*
         * Notas fiscais
         */
        final List<NotafiscalSTY> result = new LinkedList<NotafiscalSTY>();

        if (parentKnowledgeRowIdList.size() > 0) {
            final SelectNotasfiscaisQuery notasfiscaisQuery = new SelectNotasfiscaisQuery(parentKnowledgeRowIdList, retrieveSomenteNaoRecebidos);
            final SelectQueryResult<List<NotafiscalSTY>> notasfiscaisQueryResult = notasfiscaisQueryExecuter.executeSelectQuery(notasfiscaisQuery);
            if (notasfiscaisQueryResult.isQuerySuccessful() == false) {
                final SimpleServiceResponse<List<NotafiscalSTY>> errorServiceResponse = new SimpleServiceResponse<List<NotafiscalSTY>>();
                return errorServiceResponse;
            }
            result.addAll(notasfiscaisQueryResult.getResult());
        }

        final SimpleServiceResponse<List<NotafiscalSTY>> serviceResponse = new SimpleServiceResponse<List<NotafiscalSTY>>(result);
        return serviceResponse;

    }

}// class
