package com.kit.lightserver.services.db.dbd;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dajo.framework.configuration.ConfigAccessor;
import org.dajo.framework.db.DatabaseConfig;
import org.dajo.framework.db.QueryExecutor;
import org.dajo.framework.db.SimpleQueryExecutor;
import org.dajo.framework.db.UpdateQueryResult;

import com.fap.collections.SmartCollections;

import com.kit.lightserver.domain.types.FormConhecimentoRowIdSTY;
import com.kit.lightserver.domain.types.FormNotafiscalRowIdSTY;
import com.kit.lightserver.domain.types.FormSTY;
import com.kit.lightserver.services.be.common.DatabaseAliases;

public final class FormFlagsServices {

    static private final Logger LOGGER = LoggerFactory.getLogger(FormFlagsServices.class);

    static public FormFlagsServices getInstance(final ConfigAccessor configAccessor) {
        return new FormFlagsServices(configAccessor);
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private final QueryExecutor dbdQueryExecutor;

    private FormFlagsServices(final ConfigAccessor configAccessor) {
        DatabaseConfig dbdConfig = DatabaseConfig.getInstance(configAccessor,  DatabaseAliases.DBD);
        this.dbdQueryExecutor = new SimpleQueryExecutor(dbdConfig);
    }

    public boolean flagFormsAsReceived(final String ktClientId, final List<FormSTY> forms) {

        final List<FormConhecimentoRowIdSTY> conhecimentos = new LinkedList<FormConhecimentoRowIdSTY>();
        SmartCollections.specialFilter(conhecimentos, forms, new ConhecimentoRowIdFilter());

        boolean coSuccess = flagFormsConhecimentos(ktClientId, FormFlagEnum.RECEBIDO, conhecimentos);

        final List<FormNotafiscalRowIdSTY> notasfiscais = new LinkedList<FormNotafiscalRowIdSTY>();
        SmartCollections.specialFilter(notasfiscais, forms, new NotafiscalRowIdFilter());

        boolean nfSuccess = flagFormsNotasfiscais(ktClientId, FormFlagEnum.RECEBIDO, notasfiscais);

        return (coSuccess && nfSuccess);

    }

    public boolean flagFormsAsLido(final String ktClientUserId, final FormConhecimentoRowIdSTY formRowId) {
        List<FormConhecimentoRowIdSTY> list = new LinkedList<FormConhecimentoRowIdSTY>();
        list.add(formRowId);
        return flagFormsConhecimentos(ktClientUserId, FormFlagEnum.LIDO, list);
    }

    public boolean flagFormsAsEditado(final String ktClientUserId, final FormConhecimentoRowIdSTY formRowId) {
        List<FormConhecimentoRowIdSTY> list = new LinkedList<FormConhecimentoRowIdSTY>();
        list.add(formRowId);
        return flagFormsConhecimentos(ktClientUserId, FormFlagEnum.EDITADO, list);
    }

    public boolean flagEditadoNotafiscal(final String ktClientUserId, final FormNotafiscalRowIdSTY formRowId) {
        List<FormNotafiscalRowIdSTY> list = new LinkedList<FormNotafiscalRowIdSTY>();
        list.add(formRowId);
        return flagFormsNotasfiscais(ktClientUserId, FormFlagEnum.EDITADO, list);
    }

    private boolean flagFormsConhecimentos(final String ktClientUserId, final FormFlagEnum formFlag, final List<FormConhecimentoRowIdSTY> conhecimentos) {
        LOGGER.info("Updating forms flags. conhecimentos="+conhecimentos.size());
        if (conhecimentos.size() == 0) {
            LOGGER.info("No 'conhecimentos' to update.");
            return false;
        }
        else {
            final UpdateConhecimentosFlagsQuery updateConhecimentoRecebidoFlagQuery = new UpdateConhecimentosFlagsQuery(formFlag, ktClientUserId, conhecimentos);
            final UpdateQueryResult conhecimentosFlagResult = dbdQueryExecutor.executeUpdateQuery(updateConhecimentoRecebidoFlagQuery);
            if (conhecimentosFlagResult.isUpdateQuerySuccessful() == false) {
                LOGGER.error("Error updating the flag");
                return false;
            }
        }
        return true;
    }

    private boolean flagFormsNotasfiscais(final String ktClientId, final FormFlagEnum formFlag, final List<FormNotafiscalRowIdSTY> notasfiscais) {
        LOGGER.info("Updating forms flags. notasfiscais="+notasfiscais.size());
        if (notasfiscais.size() == 0) {
            LOGGER.info("No 'notasfiscais' to update.");
            return false;
        }
        else {
            final UpdateNotafiscaisFlagsQuery updateNotasfiscaisRecebidasFlagQuery = new UpdateNotafiscaisFlagsQuery(formFlag, notasfiscais);
            final UpdateQueryResult notasfiscaisFlagResult = dbdQueryExecutor.executeUpdateQuery(updateNotasfiscaisRecebidasFlagQuery);
            if (notasfiscaisFlagResult.isUpdateQuerySuccessful() == false) {
                LOGGER.error("Error updating the flag");
                return false;
            }
        }
        return true;
    }

}// class
