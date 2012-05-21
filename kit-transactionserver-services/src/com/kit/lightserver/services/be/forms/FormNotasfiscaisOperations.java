package com.kit.lightserver.services.be.forms;

import java.util.LinkedList;
import java.util.List;

import com.fap.framework.db.KitDataSource;
import com.fap.framework.db.SelectQueryResult;
import com.kit.lightserver.domain.containers.SimpleServiceResponse;
import com.kit.lightserver.domain.types.ConhecimentoSTY;
import com.kit.lightserver.domain.types.NotafiscalSTY;
import com.kit.lightserver.services.db.forms.notasfiscais.SelectNotasfiscaisQuery;
import com.kit.lightserver.services.db.forms.notasfiscais.SelectNotasfiscaisQueryResultAdapter;

final class FormNotasfiscaisOperations {

    private final KitDataSource dataSource;

    FormNotasfiscaisOperations(final KitDataSource dataSource) {
        this.dataSource = dataSource;
    }

    SimpleServiceResponse<List<NotafiscalSTY>> retrieveNotasfiscais(final List<ConhecimentoSTY> conhecimentoList,
            final boolean retrieveSomenteNaoRecebidos) {

        /*
         * Conhecimentos
         */
        final List<Integer> parentKnowledgeRowIdList = new LinkedList<Integer>();
        for (ConhecimentoSTY conhecimentoSTY : conhecimentoList) {
            parentKnowledgeRowIdList.add(conhecimentoSTY.getKtRowId());
        }

        /*
         * Notas fiscais
         */
        final List<NotafiscalSTY> result = new LinkedList<NotafiscalSTY>();

        if (parentKnowledgeRowIdList.size() > 0) {
            SelectNotasfiscaisQueryResultAdapter notasfiscaisAdapter = new SelectNotasfiscaisQueryResultAdapter();
            SelectNotasfiscaisQuery notasfiscaisQuery = new SelectNotasfiscaisQuery(parentKnowledgeRowIdList, retrieveSomenteNaoRecebidos);
            SelectQueryResult<List<NotafiscalSTY>> notasfiscaisQueryResult = dataSource.executeSelectQuery(notasfiscaisQuery, notasfiscaisAdapter);
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
