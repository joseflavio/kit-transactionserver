package com.kit.lightserver.services.be.forms;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fap.chronometer.Chronometer;
import com.fap.collections.SmartCollections;
import com.fap.framework.db.DatabaseConfig;
import com.fap.framework.db.QueryExecutor;
import com.fap.framework.db.SelectQueryResult;
import com.fap.framework.db.SimpleQueryExecutor;
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
import com.kit.lightserver.services.db.forms.conhecimentos.lido.UpdateConhecimentosFirstReadQuery;
import com.kit.lightserver.services.db.forms.notasfiscais.UpdateNotafiscaisFlagsQuery;

public final class FormServices {

    static private final Logger LOGGER = LoggerFactory.getLogger(FormServices.class);

    static public FormServices getInstance(final ConfigAccessor configAccessor) {
        DatabaseConfig dbConfig = DatabaseConfig.getInstance(configAccessor);
        QueryExecutor dataSource = new SimpleQueryExecutor(dbConfig);
        return new FormServices(dataSource);
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private final QueryExecutor dataSource;

    private final FormNotasfiscaisOperations formNotasfiscaisOperations;

    private FormServices(final QueryExecutor dataSource) {
        this.dataSource = dataSource;
        this.formNotasfiscaisOperations = new FormNotasfiscaisOperations(dataSource);
    }

    public SimpleServiceResponse<FormsParaEnviarCTX> retrieveCurrentForms(final String ktClientUserId, final boolean retrieveNaoRecebidos) {

        SelectConhecimentosQueryResultAdapter queryAdapter = new SelectConhecimentosQueryResultAdapter();
        SelectConhecimentosQuery query = new SelectConhecimentosQuery(ktClientUserId, retrieveNaoRecebidos);
        SelectQueryResult<List<ConhecimentoSTY>> conhecimentosQueryResult = dataSource.executeSelectQuery(query, queryAdapter);
        if (conhecimentosQueryResult.isSelectQuerySuccessful() == false) {
            final SimpleServiceResponse<FormsParaEnviarCTX> errorServiceResponse = new SimpleServiceResponse<FormsParaEnviarCTX>();
            return errorServiceResponse;
        }

        List<ConhecimentoSTY> conhecimentosList = conhecimentosQueryResult.getResult();
        FormServices.LOGGER.debug("conhecimentoList.size()=" + conhecimentosList.size());

        // TODO: Usar um método melhor de salvar estatisticas dos serviços
        final Chronometer c1 = new Chronometer("NotasfiscaisServices.retrieveNotasfiscais");
        c1.start();
        SimpleServiceResponse<List<NotafiscalSTY>> notasfiscaisResult = formNotasfiscaisOperations.retrieveNotasfiscais(conhecimentosList, retrieveNaoRecebidos);
        c1.stop();
        FormServices.LOGGER.info("Time to execute the service. chronometer={}", c1);

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

        UpdateConhecimentosFirstReadQuery updateQuery = new UpdateConhecimentosFirstReadQuery(ktClientId, conhecimentoIdSTY, dataDaLeitura);
        UpdateQueryResult queryResult = dataSource.executeUpdateQuery(updateQuery);
        if (queryResult.isUpdateQuerySuccessful() == false) {
            LOGGER.error("Error updating. query={}", new UpdateQueryPrinter(updateQuery));
            return false;
        }
        else {
            if (queryResult.getRowsUpdated() == 0) {
                LOGGER.error("Unexpected result updating. getRowsUpdated={}", Integer.valueOf(queryResult.getRowsUpdated()));
            }
        }

        return true;

    }

}// class
