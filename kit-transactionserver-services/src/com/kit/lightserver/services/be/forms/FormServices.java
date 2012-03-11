package com.kit.lightserver.services.be.forms;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fap.chronometer.Chronometer;
import com.fap.collections.SmartCollections;
import com.fap.framework.db.DatabaseConfig;
import com.fap.framework.db.KitDataSource;
import com.fap.framework.db.KitDataSourceSimple;
import com.fap.framework.db.SelectQueryResult;
import com.fap.framework.db.UpdateQueryPrinter;
import com.fap.framework.db.UpdateQueryResult;
import com.jfap.framework.configuration.ConfigAccessor;
import com.kit.lightserver.domain.containers.FormsParaEnviarCTX;
import com.kit.lightserver.domain.containers.SimpleServiceResponse;
import com.kit.lightserver.domain.types.ConhecimentoIdSTY;
import com.kit.lightserver.domain.types.ConhecimentoSTY;
import com.kit.lightserver.domain.types.FormSTY;
import com.kit.lightserver.domain.types.NotafiscalSTY;
import com.kit.lightserver.services.db.forms.conhecimentos.SelectConhecimentosQuery;
import com.kit.lightserver.services.db.forms.conhecimentos.SelectConhecimentosQueryResultAdapter;
import com.kit.lightserver.services.db.forms.conhecimentos.UpdateConhecimentosFlagsQuery;
import com.kit.lightserver.services.db.forms.conhecimentos.lido.SelectConhecimentoEspecifico;
import com.kit.lightserver.services.db.forms.conhecimentos.lido.SelectConhecimentoEspecificoAdapter;
import com.kit.lightserver.services.db.forms.conhecimentos.lido.UpdateConhecimentosFirstReadQuery;
import com.kit.lightserver.services.db.forms.conhecimentos.lido.UpdateConhecimentosLastReadQuery;
import com.kit.lightserver.services.db.forms.notasfiscais.UpdateNotafiscaisFlagsQuery;

public final class FormServices {

    static private final Logger LOGGER = LoggerFactory.getLogger(FormServices.class);

    static public FormServices getInstance(final ConfigAccessor configAccessor) {
        DatabaseConfig dbConfig = DatabaseConfig.getInstance(configAccessor);
        KitDataSource dataSource = new KitDataSourceSimple(dbConfig);
        return new FormServices(dataSource);
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private final KitDataSource dataSource;

    private final FormNotasfiscaisOperations formNotasfiscaisOperations;

    private FormServices(final KitDataSource dataSource) {
        this.dataSource = dataSource;
        this.formNotasfiscaisOperations = new FormNotasfiscaisOperations(dataSource);
    }

    public SimpleServiceResponse<FormsParaEnviarCTX> retrieveCurrentForms(final String ktUserClientId, final boolean retrieveNaoRecebidos) {

        SelectConhecimentosQueryResultAdapter queryAdapter = new SelectConhecimentosQueryResultAdapter();
        SelectConhecimentosQuery query = new SelectConhecimentosQuery(ktUserClientId, retrieveNaoRecebidos);
        SelectQueryResult<List<ConhecimentoSTY>> conhecimentosQueryResult = dataSource.executeSelectQuery(query, queryAdapter);
        if (conhecimentosQueryResult.isQuerySuccessful() == false) {
            final SimpleServiceResponse<FormsParaEnviarCTX> errorServiceResponse = new SimpleServiceResponse<FormsParaEnviarCTX>();
            return errorServiceResponse;
        }

        List<ConhecimentoSTY> conhecimentosList = conhecimentosQueryResult.getResult();
        FormServices.LOGGER.debug("conhecimentoList.size()=" + conhecimentosList.size());

        // TODO: Usar um método melhor de salvar estatisticas dos serviços
        final Chronometer chronometer = new Chronometer("NotasfiscaisServices.retrieveNotasfiscais");
        chronometer.start();
        SimpleServiceResponse<List<NotafiscalSTY>> notasfiscaisResult = formNotasfiscaisOperations.retrieveNotasfiscais(conhecimentosList, retrieveNaoRecebidos);
        chronometer.stop();
        FormServices.LOGGER.info("Time to execute the service. chronometer={}", chronometer);

        if (notasfiscaisResult.isValid() == false) {
            final SimpleServiceResponse<FormsParaEnviarCTX> errorServiceResponse = new SimpleServiceResponse<FormsParaEnviarCTX>();
            return errorServiceResponse;
        }

        final List<NotafiscalSTY> notasfiscaisList = notasfiscaisResult.getValidResult();
        FormServices.LOGGER.debug("notasfiscaisList.size()=" + notasfiscaisList.size());

        final FormsParaEnviarCTX formsContext = new FormsParaEnviarCTX(conhecimentosList, notasfiscaisList);

        final SimpleServiceResponse<FormsParaEnviarCTX> serviceResponse = new SimpleServiceResponse<FormsParaEnviarCTX>(formsContext);

        return serviceResponse;

    }

    public boolean flagFormsAsReceived(final String ktClientId, final List<FormSTY> forms) {

        final List<ConhecimentoSTY> conhecimentos = new LinkedList<ConhecimentoSTY>();
        SmartCollections.specialFilter(conhecimentos, forms, new ConhecimentoFilter());

        final List<NotafiscalSTY> notasfiscais = new LinkedList<NotafiscalSTY>();
        SmartCollections.specialFilter(notasfiscais, forms, new NotafiscalFilter());

        LOGGER.info("Updating forms flags. forms=" + forms.size() + ", conhecimentos=" + conhecimentos.size() + ", notasfiscais=" + notasfiscais.size());

        if (conhecimentos.size() == 0) {
            LOGGER.info("No 'conhecimentos' to update.");
        }
        else {
            final UpdateConhecimentosFlagsQuery updateConhecimentoRecebidoFlagQuery = new UpdateConhecimentosFlagsQuery("Recebido", ktClientId, conhecimentos);
            final UpdateQueryResult conhecimentosFlagResult = dataSource.executeUpdateQuery(updateConhecimentoRecebidoFlagQuery);
            if (conhecimentosFlagResult.isUpdateQuerySuccessful() == false) {
                LOGGER.error("Error updating the flag");
            }
        }

        if (notasfiscais.size() == 0) {
            LOGGER.info("No 'notasfiscais' to update.");
        }
        else {
            final UpdateNotafiscaisFlagsQuery updateNotasfiscaisRecebidasFlagQuery = new UpdateNotafiscaisFlagsQuery("Recebido", notasfiscais);
            final UpdateQueryResult notasfiscaisFlagResult = dataSource.executeUpdateQuery(updateNotasfiscaisRecebidasFlagQuery);
            if (notasfiscaisFlagResult.isUpdateQuerySuccessful() == false) {
                LOGGER.error("Error updating the flag");
            }
        }

        return true;

    }

    public boolean flagFormsAsRead(final String ktClientId, final ConhecimentoIdSTY conhecimentoIdSTY, final Date dataDaLeitura) {

        LOGGER.info("flagFormsAsRead(..) - enter");

        SelectConhecimentoEspecifico selectQuery = new SelectConhecimentoEspecifico(ktClientId, conhecimentoIdSTY);
        SelectQueryResult<Boolean> selectResult = dataSource.executeSelectQuery(selectQuery, new SelectConhecimentoEspecificoAdapter());

        LOGGER.info("selectResult={}"+selectResult);

        if( selectResult.getResult() == Boolean.FALSE ) {

            UpdateConhecimentosFirstReadQuery updateQuery = new UpdateConhecimentosFirstReadQuery(ktClientId, conhecimentoIdSTY, dataDaLeitura);
            UpdateQueryResult queryResult = dataSource.executeUpdateQuery(updateQuery);
            if (queryResult.isUpdateQuerySuccessful() == false) {
                LOGGER.error("Error updating. query={}", new UpdateQueryPrinter(updateQuery));
                return false;
            }
            else {
                if( queryResult.getRowsUpdated() == 0 ) {
                    LOGGER.warn("Unexpected result updating. getRowsUpdated={}", Integer.valueOf(queryResult.getRowsUpdated()) );
                }
            }
        }
        else {
            UpdateConhecimentosLastReadQuery updateQuery = new UpdateConhecimentosLastReadQuery(ktClientId, conhecimentoIdSTY, dataDaLeitura);
            UpdateQueryResult queryResult = dataSource.executeUpdateQuery(updateQuery);
            if (queryResult.isUpdateQuerySuccessful() == false) {
                LOGGER.error("Error updating. query={}", new UpdateQueryPrinter(updateQuery));
                return false;
            }
        }

        return true;

    }

}// class
