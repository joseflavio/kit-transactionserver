package com.kit.lightserver.services.be.forms;

import java.util.LinkedList;
import java.util.List;

import org.dajo.framework.db.QueryExecutor;
import org.dajo.framework.db.SelectQueryResult;

import com.kit.lightserver.domain.containers.SimpleServiceResponse;
import com.kit.lightserver.domain.types.ConhecimentoSTY;
import com.kit.lightserver.domain.types.FormUniqueIdSTY;
import com.kit.lightserver.domain.types.NotafiscalSTY;
import com.kit.lightserver.services.db.dbd.SelectNotasfiscaisQuery;
import com.kit.lightserver.services.db.dbd.SelectNotasfiscaisQueryResultAdapter;

final class FormNotasfiscaisOperations {

    private final QueryExecutor dbdQueryExecutor;

    FormNotasfiscaisOperations(final QueryExecutor dbdQueryExecutor) {
        this.dbdQueryExecutor = dbdQueryExecutor;
    }

    SimpleServiceResponse<List<NotafiscalSTY>> retrieveNotasfiscais(
            final String ktClientUserId,
            final List<ConhecimentoSTY> conhecimentoList,
            final boolean retrieveSomenteNaoRecebidos) {

        /*
         * Conhecimentos
         */
        final List<FormUniqueIdSTY> parentKnowledgeRowIdList = new LinkedList<FormUniqueIdSTY>();
        for (ConhecimentoSTY conhecimentoSTY : conhecimentoList) {
            parentKnowledgeRowIdList.add( conhecimentoSTY.getFormUniqueId() );
        }

        /*
         * Notas fiscais
         */
        final List<NotafiscalSTY> result = new LinkedList<NotafiscalSTY>();

        if (parentKnowledgeRowIdList.size() > 0) {
            SelectNotasfiscaisQueryResultAdapter notasfiscaisAdapter = new SelectNotasfiscaisQueryResultAdapter();
            SelectNotasfiscaisQuery notasfiscaisQuery = new SelectNotasfiscaisQuery(ktClientUserId, parentKnowledgeRowIdList, retrieveSomenteNaoRecebidos);
            SelectQueryResult<List<NotafiscalSTY>> notasfiscaisQueryResult = dbdQueryExecutor.executeSelectQuery(notasfiscaisQuery, notasfiscaisAdapter);
            if (notasfiscaisQueryResult.isSelectQuerySuccessful() == false) {
                final SimpleServiceResponse<List<NotafiscalSTY>> errorServiceResponse = new SimpleServiceResponse<List<NotafiscalSTY>>();
                return errorServiceResponse;
            }
            result.addAll(notasfiscaisQueryResult.getResult());
        }

        final SimpleServiceResponse<List<NotafiscalSTY>> serviceResponse = new SimpleServiceResponse<List<NotafiscalSTY>>(result);
        return serviceResponse;

    }

}// class
