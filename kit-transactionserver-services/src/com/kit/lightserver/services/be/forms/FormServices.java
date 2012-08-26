package com.kit.lightserver.services.be.forms;

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

import com.kit.lightserver.domain.containers.FormsParaEnviarCTX;
import com.kit.lightserver.domain.containers.SimpleServiceResponse;
import com.kit.lightserver.domain.types.ConhecimentoSTY;
import com.kit.lightserver.domain.types.DataEntregaSTY;
import com.kit.lightserver.domain.types.FormClientRowIdSTY;
import com.kit.lightserver.domain.types.FormFirstReadDateSTY;
import com.kit.lightserver.domain.types.NotafiscalSTY;
import com.kit.lightserver.domain.types.StatusEntregaEnumSTY;
import com.kit.lightserver.services.be.common.DatabaseAliases;
import com.kit.lightserver.services.db.dbd.FormFlagsServices;
import com.kit.lightserver.services.db.dbd.SelectConhecimentosQuery;
import com.kit.lightserver.services.db.dbd.SelectConhecimentosQueryResultAdapter;
import com.kit.lightserver.services.db.dbl.InsertFormFieldDateQuery;
import com.kit.lightserver.services.db.dbl.InsertFormFieldString32Query;

public final class FormServices {

    static private final int MAX_RETRIEVE_CONHECIMENTOS = 250;

    static private final Logger LOGGER = LoggerFactory.getLogger(FormServices.class);

    static public FormServices getInstance(final ConfigAccessor configAccessor) {
        return new FormServices(configAccessor);
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private final QueryExecutor dbdQueryExecutor;
    private final QueryExecutor dblQueryExecutor;

    private final FormNotasfiscaisOperations formNotasfiscaisOperations;

    private final FormFlagsServices formFlagsServices;

    private FormServices(final ConfigAccessor configAccessor) {

        this.formFlagsServices = FormFlagsServices.getInstance(configAccessor);

        DatabaseConfig dbdConfig = DatabaseConfig.getInstance(configAccessor,  DatabaseAliases.DBD);
        this.dbdQueryExecutor = new SimpleQueryExecutor(dbdConfig);

        DatabaseConfig dblConfig = DatabaseConfig.getInstance(configAccessor,  DatabaseAliases.DBL);
        this.dblQueryExecutor = new SimpleQueryExecutor(dblConfig);

        this.formNotasfiscaisOperations = new FormNotasfiscaisOperations(dbdQueryExecutor);

    }

    public SimpleServiceResponse<FormsParaEnviarCTX> retrieveCurrentForms(final String ktClientUserId, final boolean retrieveNaoRecebidos) {

        SelectConhecimentosQueryResultAdapter queryAdapter = new SelectConhecimentosQueryResultAdapter();
        SelectConhecimentosQuery query = new SelectConhecimentosQuery(ktClientUserId, retrieveNaoRecebidos, MAX_RETRIEVE_CONHECIMENTOS);
        SelectQueryResult<List<ConhecimentoSTY>> conhecimentosQueryResult = dbdQueryExecutor.executeSelectQuery(query, queryAdapter);
        if (conhecimentosQueryResult.isSelectQuerySuccessful() == false) {
            final SimpleServiceResponse<FormsParaEnviarCTX> errorServiceResponse = new SimpleServiceResponse<FormsParaEnviarCTX>();
            return errorServiceResponse;
        }

        List<ConhecimentoSTY> conhecimentosList = conhecimentosQueryResult.getResult();
        FormServices.LOGGER.info("conhecimentoList.size()=" + conhecimentosList.size());

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
        FormServices.LOGGER.info("notasfiscaisList.size()=" + notasfiscaisList.size());

        final FormsParaEnviarCTX formsContext = new FormsParaEnviarCTX(conhecimentosList, notasfiscaisList);

        final SimpleServiceResponse<FormsParaEnviarCTX> serviceResponse = new SimpleServiceResponse<FormsParaEnviarCTX>(formsContext);

        return serviceResponse;

    }

    public boolean saveFormFirstRead(final String ktClientUserId, final FormClientRowIdSTY clientRowId, final FormFirstReadDateSTY formFirstReadDateSTY) {

        boolean flagSuccess = formFlagsServices.flagFormsAsLido(ktClientUserId, clientRowId);
        if( flagSuccess == false ) {
            return false;
        }

        InsertFormFieldDateQuery insertQuery = new InsertFormFieldDateQuery(
                clientRowId, "PRIMEIRA_LEITURA", formFirstReadDateSTY.getDate(), formFirstReadDateSTY.toString());

        InsertQueryResult queryResult = dblQueryExecutor.executeInsertQuery(insertQuery);

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

    public boolean saveFormEdited(final String ktClientUserId, final FormClientRowIdSTY formClientRowId, final StatusEntregaEnumSTY statusEntregaEnumSTY,
            final DataEntregaSTY dataEntrega) {

        final boolean success = formFlagsServices.flagFormsAsEditado(ktClientUserId, formClientRowId);

        if( success == false ) {
            return false;
        }

        InsertFormFieldDateQuery insertDataEntregaQuery = new InsertFormFieldDateQuery(
                formClientRowId, "DATA_DA_ENTREGA", dataEntrega.getDataEntregaDate(), dataEntrega.toString());

        InsertQueryResult dataEntregaResult = dblQueryExecutor.executeInsertQuery(insertDataEntregaQuery);

        if (dataEntregaResult.isInsertQuerySuccessfull() == false) {
            LOGGER.error("Error updating. query={}", new InsertQueryPrinter(insertDataEntregaQuery));
            return false;
        }
        else if (dataEntregaResult.getRowsInserted() != 1) {
            LOGGER.error("Unexpected result updating. getRowsUpdated={}", Integer.valueOf(dataEntregaResult.getRowsInserted()));
            return false;
        }

        InsertFormFieldString32Query insertStatusEntregaQuery = new InsertFormFieldString32Query(
                formClientRowId, "STATUS_DA_ENTREGA", statusEntregaEnumSTY.getCode(), statusEntregaEnumSTY.toString());

        InsertQueryResult statusEntregaResult = dblQueryExecutor.executeInsertQuery(insertStatusEntregaQuery);

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
