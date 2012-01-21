package com.kit.lightserver.services.be.forms;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfap.chronometer.Chronometer;
import com.jfap.util.collections.SmartCollections;
import com.kit.lightserver.domain.containers.FormsCTX;
import com.kit.lightserver.domain.containers.SimpleServiceResponse;
import com.kit.lightserver.domain.types.ConhecimentoSTY;
import com.kit.lightserver.domain.types.FormSTY;
import com.kit.lightserver.domain.types.NotafiscalSTY;
import com.kit.lightserver.services.db.QueryResultContainer;
import com.kit.lightserver.services.db.SelectQueryExecuter;
import com.kit.lightserver.services.db.UpdateQueryExecuter;
import com.kit.lightserver.services.db.UpdateQueryResult;
import com.kit.lightserver.services.db.forms.conhecimentos.SelectConhecimentosQuery;
import com.kit.lightserver.services.db.forms.conhecimentos.SelectConhecimentosQueryResultAdapter;
import com.kit.lightserver.services.db.forms.conhecimentos.UpdateConhecimentosFlagsQuery;
import com.kit.lightserver.services.db.forms.notasfiscais.UpdateNotafiscaisFlagsQuery;

public final class FormServices {

    static private final Logger LOGGER = LoggerFactory.getLogger(FormServices.class);

    static public SimpleServiceResponse<FormsCTX> retrieveFormContext(final String ktUserClientId) {

        SelectConhecimentosQueryResultAdapter conhecimentosAdapter = new SelectConhecimentosQueryResultAdapter();
        SelectConhecimentosQuery conhecimentosQuery = new SelectConhecimentosQuery(ktUserClientId);

        SelectQueryExecuter<List<ConhecimentoSTY>> conhecimentosQueryExecuter = new SelectQueryExecuter<List<ConhecimentoSTY>>(conhecimentosAdapter);
        QueryResultContainer<List<ConhecimentoSTY>> conhecimentosQueryResult = conhecimentosQueryExecuter.executeSelectQuery(conhecimentosQuery);
        if (conhecimentosQueryResult.isQuerySuccessful() == false) {
            final SimpleServiceResponse<FormsCTX> errorServiceResponse = new SimpleServiceResponse<FormsCTX>();
            return errorServiceResponse;
        }



        List<ConhecimentoSTY> conhecimentoList = conhecimentosQueryResult.getResult();

        //TODO: Usar um método melhor de salvar estatisticas dos serviços
        final Chronometer chronometer = new Chronometer();
        chronometer.start();
        SimpleServiceResponse<List<NotafiscalSTY>> notasfiscaisResult = NotasfiscaisServices.retrieveNotasfiscais(conhecimentoList);
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
