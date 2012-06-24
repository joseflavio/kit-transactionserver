package com.kit.lightserver.services.be.forms;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dajo.chronometer.Chronometer;
import org.dajo.framework.configuration.ConfigAccessor;
import org.dajo.framework.db.DatabaseConfig;
import org.dajo.framework.db.QueryExecutor;
import org.dajo.framework.db.SelectQueryResult;
import org.dajo.framework.db.SimpleQueryExecutor;
import org.dajo.framework.db.UpdateQueryPrinter;
import org.dajo.framework.db.UpdateQueryResult;

import com.fap.collections.SmartCollections;

import com.kit.lightserver.domain.containers.FormsParaEnviarCTX;
import com.kit.lightserver.domain.containers.SimpleServiceResponse;
import com.kit.lightserver.domain.types.ConhecimentoIdSTY;
import com.kit.lightserver.domain.types.ConhecimentoSTY;
import com.kit.lightserver.domain.types.FormSTY;
import com.kit.lightserver.domain.types.NotafiscalSTY;
import com.kit.lightserver.services.be.common.DatabaseAliases;
import com.kit.lightserver.services.db.forms.conhecimentos.SelectConhecimentosQuery;
import com.kit.lightserver.services.db.forms.conhecimentos.SelectConhecimentosQueryResultAdapter;
import com.kit.lightserver.services.db.forms.conhecimentos.UpdateConhecimentosFlagsQuery;
import com.kit.lightserver.services.db.forms.conhecimentos.lido.UpdateConhecimentosFirstReadQuery;
import com.kit.lightserver.services.db.forms.notasfiscais.UpdateNotafiscaisFlagsQuery;

public final class FormServices {

    static private final Logger LOGGER = LoggerFactory.getLogger(FormServices.class);

    static public FormServices getInstance(final ConfigAccessor configAccessor) {
        DatabaseConfig dbdConfig = DatabaseConfig.getInstance(configAccessor,  DatabaseAliases.DBD);
        QueryExecutor dbdQueryExecutor = new SimpleQueryExecutor(dbdConfig);
        return new FormServices(dbdQueryExecutor);
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private final QueryExecutor dbdQueryExecutor;

    private final FormNotasfiscaisOperations formNotasfiscaisOperations;

    private FormServices(final QueryExecutor dbdQueryExecutor) {
        this.dbdQueryExecutor = dbdQueryExecutor;
        this.formNotasfiscaisOperations = new FormNotasfiscaisOperations(dbdQueryExecutor);
    }

    public SimpleServiceResponse<FormsParaEnviarCTX> retrieveCurrentForms(final String ktClientUserId, final boolean retrieveNaoRecebidos) {

        SelectConhecimentosQueryResultAdapter queryAdapter = new SelectConhecimentosQueryResultAdapter();
        SelectConhecimentosQuery query = new SelectConhecimentosQuery(ktClientUserId, retrieveNaoRecebidos);
        SelectQueryResult<List<ConhecimentoSTY>> conhecimentosQueryResult = dbdQueryExecutor.executeSelectQuery(query, queryAdapter);
        if (conhecimentosQueryResult.isSelectQuerySuccessful() == false) {
            final SimpleServiceResponse<FormsParaEnviarCTX> errorServiceResponse = new SimpleServiceResponse<FormsParaEnviarCTX>();
            return errorServiceResponse;
        }

        List<ConhecimentoSTY> conhecimentosList = conhecimentosQueryResult.getResult();
        FormServices.LOGGER.debug("conhecimentoList.size()=" + conhecimentosList.size());

        // TODO: Usar um método melhor de salvar estatisticas dos serviços
        final Chronometer c1 = new Chronometer("NotasfiscaisServices.retrieveNotasfiscais");
        c1.start();
        SimpleServiceResponse<List<NotafiscalSTY>> notasfiscaisResult =
                formNotasfiscaisOperations.retrieveNotasfiscais(ktClientUserId, conhecimentosList, retrieveNaoRecebidos);
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
            final UpdateQueryResult conhecimentosFlagResult = dbdQueryExecutor.executeUpdateQuery(updateConhecimentoRecebidoFlagQuery);
            if (conhecimentosFlagResult.isUpdateQuerySuccessful() == false) {
                LOGGER.error("Error updating the flag");
            }
        }

        if (notasfiscais.size() == 0) {
            LOGGER.info("No 'notasfiscais' to update.");
        }
        else {
            final UpdateNotafiscaisFlagsQuery updateNotasfiscaisRecebidasFlagQuery = new UpdateNotafiscaisFlagsQuery("Recebido", notasfiscais);
            final UpdateQueryResult notasfiscaisFlagResult = dbdQueryExecutor.executeUpdateQuery(updateNotasfiscaisRecebidasFlagQuery);
            if (notasfiscaisFlagResult.isUpdateQuerySuccessful() == false) {
                LOGGER.error("Error updating the flag");
            }
        }

        return true;

    }

    public boolean flagFormsAsRead(final String ktClientId, final ConhecimentoIdSTY conhecimentoIdSTY, final Date dataDaLeitura) {

        LOGGER.info("flagFormsAsRead(..) - enter");

        UpdateConhecimentosFirstReadQuery updateQuery = new UpdateConhecimentosFirstReadQuery(ktClientId, conhecimentoIdSTY, dataDaLeitura);
        UpdateQueryResult queryResult = dbdQueryExecutor.executeUpdateQuery(updateQuery);
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
