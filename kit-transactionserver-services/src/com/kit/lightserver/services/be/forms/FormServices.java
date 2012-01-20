package com.kit.lightserver.services.be.forms;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfap.chronometer.Chronometer;
import com.jfap.util.collections.SmartCollections;
import com.kit.lightserver.domain.ConhecimentoSTY;
import com.kit.lightserver.domain.FormSTY;
import com.kit.lightserver.domain.NotafiscalSTY;
import com.kit.lightserver.services.db.QueryResultContainer;
import com.kit.lightserver.services.db.SelectQueryExecuter;
import com.kit.lightserver.services.db.UpdateQueryExecuter;
import com.kit.lightserver.services.db.UpdateQueryResult;
import com.kit.lightserver.services.db.conhecimentos.SelectConhecimentosQuery;
import com.kit.lightserver.services.db.conhecimentos.SelectConhecimentosQueryResultAdapter;
import com.kit.lightserver.services.db.conhecimentos.UpdateConhecimentosFlagsQuery;
import com.kit.lightserver.services.db.forms.notasfiscais.SelectNotasfiscaisQuery;
import com.kit.lightserver.services.db.forms.notasfiscais.SelectNotasfiscaisQueryResultAdapter;
import com.kit.lightserver.services.db.forms.notasfiscais.UpdateNotafiscaisFlagsQuery;

public final class FormServices {

    static private final Logger LOGGER = LoggerFactory.getLogger(FormServices.class);

    static public SimpleServiceResponse<FormsCTX> retrieveFormContext(final String ktUserClientId) {

        final SelectConhecimentosQueryResultAdapter conhecimentosAdapter = new SelectConhecimentosQueryResultAdapter();
        final SelectConhecimentosQuery conhecimentosQuery = new SelectConhecimentosQuery(ktUserClientId);

        final SelectQueryExecuter<List<ConhecimentoSTY>> conhecimentosQueryExecuter = new SelectQueryExecuter<List<ConhecimentoSTY>>(conhecimentosAdapter);
        final QueryResultContainer<List<ConhecimentoSTY>> conhecimentosQueryResult = conhecimentosQueryExecuter.executeSelectQuery(conhecimentosQuery);
        if (conhecimentosQueryResult.isQuerySuccessful() == false) {
            final SimpleServiceResponse<FormsCTX> errorServiceResponse = new SimpleServiceResponse<FormsCTX>();
            return errorServiceResponse;
        }

        final Chronometer chronometer = new Chronometer();

        final List<ConhecimentoSTY> conhecimentoList = conhecimentosQueryResult.getResult();

        chronometer.start();
        SimpleServiceResponse<List<NotafiscalSTY>> notasfiscaisResult = FormServices.retrieveNotasfiscais(conhecimentoList);
        chronometer.stop();
        FormServices.LOGGER.info("service time: " + chronometer.getElapsedTime());

        if (notasfiscaisResult.isValid() == false) {
            final SimpleServiceResponse<FormsCTX> errorServiceResponse = new SimpleServiceResponse<FormsCTX>();
            return errorServiceResponse;
        }

        final List<NotafiscalSTY> notasfiscaisList = notasfiscaisResult.getValidResult();
        FormServices.LOGGER.info("notasfiscaisList.size()=" + notasfiscaisList.size());

        final FormsCTX formsContext = new FormsCTX(conhecimentoList, notasfiscaisList);

        final SimpleServiceResponse<FormsCTX> serviceResponse = new SimpleServiceResponse<FormsCTX>(formsContext);

        return serviceResponse;

    }

    static SimpleServiceResponse<List<NotafiscalSTY>> retrieveNotasfiscais(final List<ConhecimentoSTY> conhecimentoList) {

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

        if( parentKnowledgeRowIdList.size() > 0 ) {
            final SelectNotasfiscaisQuery notasfiscaisQuery = new SelectNotasfiscaisQuery(parentKnowledgeRowIdList);
            final QueryResultContainer<List<NotafiscalSTY>> notasfiscaisQueryResult = notasfiscaisQueryExecuter.executeSelectQuery(notasfiscaisQuery);
            if (notasfiscaisQueryResult.isQuerySuccessful() == false) {
                final SimpleServiceResponse<List<NotafiscalSTY>> errorServiceResponse = new SimpleServiceResponse<List<NotafiscalSTY>>();
                return errorServiceResponse;
            }
            result.addAll(notasfiscaisQueryResult.getResult());
        }

        final SimpleServiceResponse<List<NotafiscalSTY>> serviceResponse = new SimpleServiceResponse<List<NotafiscalSTY>>(result);
        return serviceResponse;

    }

    static public boolean flagFormsAsReceived(final String ktClientId, final List<FormSTY> forms) {

        final List<ConhecimentoSTY> conhecimentos = new LinkedList<ConhecimentoSTY>();
        SmartCollections.specialFilter(conhecimentos, forms, new ConhecimentoFilter());

        final List<NotafiscalSTY> notasfiscais = new LinkedList<NotafiscalSTY>();
        SmartCollections.specialFilter(notasfiscais, forms, new NotafiscalFilter());

        LOGGER.info("Updating forms flags. forms="+forms.size()+", conhecimentos="+conhecimentos.size()+", notasfiscais="+notasfiscais.size());

        final Chronometer chronometer = new Chronometer();
        chronometer.start();

        if( conhecimentos.size() == 0 ) {
            LOGGER.info("No 'conhecimentos' to update.");
        }
        else {
            final UpdateConhecimentosFlagsQuery updateConhecimentoRecebidoFlagQuery = new UpdateConhecimentosFlagsQuery("Recebido", ktClientId, conhecimentos);
            final UpdateQueryResult conhecimentosFlagResult = UpdateQueryExecuter.executeUpdateQuery(updateConhecimentoRecebidoFlagQuery);
            if( conhecimentosFlagResult.isUpdateQuerySuccessful() == false ) {
                LOGGER.error("Error updating the flag");
            }
        }

        if( notasfiscais.size() == 0 ) {
            LOGGER.info("No 'notasfiscais' to update.");
        }
        else {
            final UpdateNotafiscaisFlagsQuery updateNotasfiscaisRecebidasFlagQuery = new UpdateNotafiscaisFlagsQuery("Recebido", notasfiscais);
            final UpdateQueryResult notasfiscaisFlagResult = UpdateQueryExecuter.executeUpdateQuery(updateNotasfiscaisRecebidasFlagQuery);
            if( notasfiscaisFlagResult.isUpdateQuerySuccessful() == false ) {
                LOGGER.error("Error updating the flag");
            }
        }

        chronometer.stop();
        LOGGER.info("Service time: " + chronometer.getElapsedTime());

        return true;
    }

}// class
