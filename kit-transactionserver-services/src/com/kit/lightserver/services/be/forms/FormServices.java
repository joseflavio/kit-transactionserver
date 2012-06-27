package com.kit.lightserver.services.be.forms;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dajo.chronometer.Chronometer;
import org.dajo.framework.configuration.ConfigAccessor;
import org.dajo.framework.db.DatabaseConfig;
import org.dajo.framework.db.InsertQueryPrinter;
import org.dajo.framework.db.InsertQueryResult;
import org.dajo.framework.db.QueryExecutor;
import org.dajo.framework.db.SelectQueryResult;
import org.dajo.framework.db.SimpleQueryExecutor;
import org.dajo.framework.db.UpdateQueryResult;

import com.fap.collections.SmartCollections;

import com.kit.lightserver.domain.containers.FormsParaEnviarCTX;
import com.kit.lightserver.domain.containers.SimpleServiceResponse;
import com.kit.lightserver.domain.types.ConhecimentoSTY;
import com.kit.lightserver.domain.types.DataEntregaSTY;
import com.kit.lightserver.domain.types.FormConhecimentoRowIdSTY;
import com.kit.lightserver.domain.types.FormFirstReadDateSTY;
import com.kit.lightserver.domain.types.FormNotafiscalRowIdSTY;
import com.kit.lightserver.domain.types.FormSTY;
import com.kit.lightserver.domain.types.FormTypeEnumSTY;
import com.kit.lightserver.domain.types.NotafiscalSTY;
import com.kit.lightserver.domain.types.StatusEntregaEnumSTY;
import com.kit.lightserver.services.be.common.DatabaseAliases;
import com.kit.lightserver.services.db.forms.conhecimentos.SelectConhecimentosQuery;
import com.kit.lightserver.services.db.forms.conhecimentos.SelectConhecimentosQueryResultAdapter;
import com.kit.lightserver.services.db.forms.conhecimentos.UpdateConhecimentosFlagsQuery;
import com.kit.lightserver.services.db.forms.conhecimentos.lido.InsertFormFieldDateQuery;
import com.kit.lightserver.services.db.forms.conhecimentos.lido.InsertFormFieldString32Query;
import com.kit.lightserver.services.db.forms.notasfiscais.UpdateNotafiscaisFlagsQuery;

public final class FormServices {

    static private final Logger LOGGER = LoggerFactory.getLogger(FormServices.class);

    static public FormServices getInstance(final ConfigAccessor configAccessor) {


        return new FormServices(configAccessor);
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private final QueryExecutor dbdQueryExecutor;
    private final QueryExecutor dbfQueryExecutor;

    private final FormNotasfiscaisOperations formNotasfiscaisOperations;

    private FormServices(final ConfigAccessor configAccessor) {

        DatabaseConfig dbdConfig = DatabaseConfig.getInstance(configAccessor,  DatabaseAliases.DBD);
        this.dbdQueryExecutor = new SimpleQueryExecutor(dbdConfig);

        DatabaseConfig dbfConfig = DatabaseConfig.getInstance(configAccessor,  DatabaseAliases.DBF);
        this.dbfQueryExecutor = new SimpleQueryExecutor(dbfConfig);

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
        return flagForms(ktClientId, FormFlagEnum.RECEBIDO, forms);
    }

    private boolean flagFormsAsLido(final String ktClientId, final FormConhecimentoRowIdSTY formRowId) {
        // TODO Auto-generated method stub
        return false;
    }

    private boolean flagForms(final String ktClientId, final FormFlagEnum formFlag, final List<FormSTY> forms) {

        final List<FormConhecimentoRowIdSTY> conhecimentos = new LinkedList<FormConhecimentoRowIdSTY>();
        SmartCollections.specialFilter(conhecimentos, forms, new ConhecimentoRowIdFilter());

        final List<FormNotafiscalRowIdSTY> notasfiscais = new LinkedList<FormNotafiscalRowIdSTY>();
        SmartCollections.specialFilter(notasfiscais, forms, new NotafiscalFilter());


        LOGGER.info("Updating forms flags. forms=" + forms.size() + ", conhecimentos=" + conhecimentos.size() + ", notasfiscais=" + notasfiscais.size());

        /*
         * Conhecimentos
         */
        if (conhecimentos.size() == 0) {
            LOGGER.info("No 'conhecimentos' to update.");
        }
        else {
            final UpdateConhecimentosFlagsQuery updateConhecimentoRecebidoFlagQuery = new UpdateConhecimentosFlagsQuery(formFlag, ktClientId, conhecimentos);
            final UpdateQueryResult conhecimentosFlagResult = dbdQueryExecutor.executeUpdateQuery(updateConhecimentoRecebidoFlagQuery);
            if (conhecimentosFlagResult.isUpdateQuerySuccessful() == false) {
                LOGGER.error("Error updating the flag");
            }
        }

        /*
         * Notasfiscais
         */
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

    public boolean saveFormFirstRead(final String ktClientId, final FormConhecimentoRowIdSTY formRowId, final FormFirstReadDateSTY formFirstReadDateSTY) {

        boolean flagSuccess = flagFormsAsLido(ktClientId, formRowId);

        InsertFormFieldDateQuery insertQuery = new InsertFormFieldDateQuery(FormTypeEnumSTY.CO, formRowId, "PRIMEIRA_LEITURA",
                formFirstReadDateSTY.getDate(), formFirstReadDateSTY.toString());

        InsertQueryResult queryResult = dbfQueryExecutor.executeInsertQuery(insertQuery);

        if (queryResult.isInsertQuerySuccessfull() == false) {
            LOGGER.error("Error updating. query={}", new InsertQueryPrinter(insertQuery));
            return false;
        }
        else if (queryResult.getRowsInserted() != 1) {
            LOGGER.error("Unexpected result updating. getRowsUpdated={}", Integer.valueOf(queryResult.getRowsInserted()));
            return false;
        }

        return true;

    }

    public boolean saveFormEntregaFields(final String ktClientId, final FormConhecimentoRowIdSTY formRowId, final StatusEntregaEnumSTY statusEntregaEnumSTY,
            final DataEntregaSTY dataEntrega) {

        InsertFormFieldDateQuery insertDataEntregaQuery = new InsertFormFieldDateQuery(FormTypeEnumSTY.CO, formRowId, "DATA_DA_ENTREGA",
                dataEntrega.getDataEntregaDate(), dataEntrega.toString());

        InsertQueryResult dataEntregaResult = dbfQueryExecutor.executeInsertQuery(insertDataEntregaQuery);

        if (dataEntregaResult.isInsertQuerySuccessfull() == false) {
            LOGGER.error("Error updating. query={}", new InsertQueryPrinter(insertDataEntregaQuery));
            return false;
        }
        else if (dataEntregaResult.getRowsInserted() != 1) {
            LOGGER.error("Unexpected result updating. getRowsUpdated={}", Integer.valueOf(dataEntregaResult.getRowsInserted()));
            return false;
        }

        InsertFormFieldString32Query insertStatusEntregaQuery = new InsertFormFieldString32Query(FormTypeEnumSTY.CO, formRowId, "STATUS_DA_ENTREGA",
                statusEntregaEnumSTY.getDatabaseCode(), statusEntregaEnumSTY.toString());

        InsertQueryResult statusEntregaResult = dbfQueryExecutor.executeInsertQuery(insertStatusEntregaQuery);

        if (statusEntregaResult.isInsertQuerySuccessfull() == false) {
            LOGGER.error("Error updating. query={}", new InsertQueryPrinter(insertDataEntregaQuery));
            return false;
        }
        else if (statusEntregaResult.getRowsInserted() != 1) {
            LOGGER.error("Unexpected result updating. getRowsUpdated={}", Integer.valueOf(dataEntregaResult.getRowsInserted()));
            return false;
        }

        return true;

    }

}// class
