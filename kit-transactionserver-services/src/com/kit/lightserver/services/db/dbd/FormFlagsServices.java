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

import com.kit.lightserver.domain.filters.ConhecimentoRowIdTransformFilter;
import com.kit.lightserver.domain.filters.FormRowIdFilter;
import com.kit.lightserver.domain.filters.NotafiscalRowIdTransformFilter;
import com.kit.lightserver.domain.types.FormClientRowIdSTY;
import com.kit.lightserver.domain.types.FormSTY;
import com.kit.lightserver.domain.types.TemplateEnumSTY;
import com.kit.lightserver.services.be.common.DatabaseAliases;

public final class FormFlagsServices {

    static private final Logger LOGGER = LoggerFactory.getLogger(FormFlagsServices.class);

    static public FormFlagsServices getInstance(final ConfigAccessor configAccessor) {
        return new FormFlagsServices(configAccessor);
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private final QueryExecutor dbdQueryExecutor;

    private FormFlagsServices(final ConfigAccessor configAccessor) {
        DatabaseConfig dbdConfig = DatabaseConfig.getInstance(configAccessor,  DatabaseAliases.DBD, false);
        this.dbdQueryExecutor = new SimpleQueryExecutor(dbdConfig);
    }

    public boolean flagFormsAsReceived(final String ktClientId, final List<FormSTY> forms) {

        final List<FormClientRowIdSTY> conhecimentos = new LinkedList<>();
        SmartCollections.specialFilter2(conhecimentos, forms, new ConhecimentoRowIdTransformFilter());

        boolean coSuccess = flagFormsByClientRowId(ktClientId, FormFlagEnum.RECEBIDO, conhecimentos);

        final List<FormClientRowIdSTY> notasfiscais = new LinkedList<>();
        SmartCollections.specialFilter2(notasfiscais, forms, new NotafiscalRowIdTransformFilter());

        boolean nfSuccess = flagFormsByClientRowId(ktClientId, FormFlagEnum.RECEBIDO, notasfiscais);

        return (coSuccess && nfSuccess);

    }

    public boolean flagFormsAsLido(final String ktClientUserId, final FormClientRowIdSTY formRowId) {
        List<FormClientRowIdSTY> list = new LinkedList<FormClientRowIdSTY>();
        list.add(formRowId);
        return flagFormsByClientRowId(ktClientUserId, FormFlagEnum.LIDO, list);
    }

    public boolean flagFormsAsEditado(final String ktClientUserId, final FormClientRowIdSTY formRowId) {
        List<FormClientRowIdSTY> list = new LinkedList<FormClientRowIdSTY>();
        list.add(formRowId);
        return flagFormsByClientRowId(ktClientUserId, FormFlagEnum.EDITADO, list);
    }

    private boolean flagFormsByClientRowId(final String ktClientUserId, final FormFlagEnum formFlag, final List<FormClientRowIdSTY> forms) {

        LOGGER.info("Updating forms flags. forms="+forms.size());

        if (forms.size() == 0) { LOGGER.warn("No forms to update."); return false; }

        FormRowIdFilter conhecimentosFilter = new FormRowIdFilter(TemplateEnumSTY.CO);
        List<FormClientRowIdSTY> conhecimentos = new LinkedList<>();
        SmartCollections.filter2(conhecimentos, forms, conhecimentosFilter);

        if( conhecimentos.size() > 0 ) {
            LOGGER.info("Updating forms flags. conhecimentos="+conhecimentos.size());
            UpdateConhecimentosFlagByClientIdQuery updateConhecimentoRecebidoFlagQuery = new UpdateConhecimentosFlagByClientIdQuery(formFlag, ktClientUserId, conhecimentos);
            UpdateQueryResult conhecimentosFlagResult = dbdQueryExecutor.executeUpdateQuery(updateConhecimentoRecebidoFlagQuery);
            if (conhecimentosFlagResult.isUpdateQuerySuccessful() == false) {
                LOGGER.error("Error updating the flag");
                return false;
            }
        }

        FormRowIdFilter notasfiscaisFilter = new FormRowIdFilter(TemplateEnumSTY.CO);
        List<FormClientRowIdSTY> notasfiscais = new LinkedList<>();
        SmartCollections.filter2(notasfiscais, forms, notasfiscaisFilter);

        if( notasfiscais.size() > 0 ) {
            LOGGER.info("Updating forms flags. notasfiscais="+notasfiscais.size());
            final UpdateNotasfiscaisFlagByClientRowIdQuery updateNotasfiscaisRecebidasFlagQuery = new UpdateNotasfiscaisFlagByClientRowIdQuery(formFlag, notasfiscais);
            final UpdateQueryResult notasfiscaisFlagResult = dbdQueryExecutor.executeUpdateQuery(updateNotasfiscaisRecebidasFlagQuery);
            if (notasfiscaisFlagResult.isUpdateQuerySuccessful() == false) {
                LOGGER.error("Error updating the flag");
                return false;
            }
        }

        return true;
    }

}// class
