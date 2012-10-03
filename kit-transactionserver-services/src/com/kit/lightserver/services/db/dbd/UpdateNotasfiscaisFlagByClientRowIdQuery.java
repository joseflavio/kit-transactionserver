package com.kit.lightserver.services.db.dbd;

import java.util.LinkedList;
import java.util.List;

import org.dajo.framework.db.QueryIntParameter;
import org.dajo.framework.db.QueryParameter;
import org.dajo.framework.db.QueryStringUtils;
import org.dajo.framework.db.UpdateQueryInterface;

import com.kit.lightserver.domain.types.FormClientRowIdSTY;

final class UpdateNotasfiscaisFlagByClientRowIdQuery implements UpdateQueryInterface {

    private final String flagName;

    private final String rowIdsOrClause;

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    private final String flagColumnDbUpdateTime;

    public UpdateNotasfiscaisFlagByClientRowIdQuery(final FormFlagEnum flagName, final List<FormClientRowIdSTY> notasfiscaisList) {

        if( notasfiscaisList == null || notasfiscaisList.size() == 0 ) {
            throw new RuntimeException("conhecimentosList can not be empty.");
        }

        this.flagName = flagName.getDbFlagColumnName();
        this.flagColumnDbUpdateTime = flagName.getDbFlagColumnName()+"UpdateDBTime";

        this.rowIdsOrClause = QueryStringUtils.buildLongOrClause("KTRowId", notasfiscaisList.size());

        for (final FormClientRowIdSTY notafiscalSTY : notasfiscaisList) {
            assert( FormClientRowIdSTY.isNotafiscal(notafiscalSTY) == true );
            final int currentKtRowId = notafiscalSTY.getKtFormRowId();
            final QueryIntParameter ktRowIdParam = new QueryIntParameter(currentKtRowId);
            queryParameters.add(ktRowIdParam);
        }


    }// constructor

    @Override
    public String getPreparedUpdateQueryString() {

        final String queryStr =
                "UPDATE " + DBDTables.NOTASFISCAIS.TABLE_NAME + " SET "+ flagName + "=1, "+ flagColumnDbUpdateTime + "=SYSUTCDATETIME() WHERE " + rowIdsOrClause;

        return queryStr;
    }

    @Override
    public List<QueryParameter> getUpdateQueryParameters() {
        return queryParameters;
    }

}// class
