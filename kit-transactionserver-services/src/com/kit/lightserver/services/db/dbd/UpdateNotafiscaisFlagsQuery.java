package com.kit.lightserver.services.db.dbd;

import java.util.LinkedList;
import java.util.List;

import org.dajo.framework.db.QueryIntParameter;
import org.dajo.framework.db.QueryParameter;
import org.dajo.framework.db.UpdateQueryInterface;
import org.dajo.framework.db.util.QueryUtil;

import com.kit.lightserver.domain.types.FormNotafiscalRowIdSTY;

final class UpdateNotafiscaisFlagsQuery implements UpdateQueryInterface {

    private final String flagName;

    private final String rowIdsOrClause;

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    private final String flagColumnDbUpdateTime;

    public UpdateNotafiscaisFlagsQuery(final FormFlagEnum flagName, final List<FormNotafiscalRowIdSTY> notasfiscaisList) {

        if( notasfiscaisList == null || notasfiscaisList.size() == 0 ) {
            throw new RuntimeException("conhecimentosList can not be empty.");
        }

        this.flagName = flagName.getDbFlagColumnName();
        this.flagColumnDbUpdateTime = flagName.getDbFlagColumnName()+"UpdateDBTime";

        this.rowIdsOrClause = QueryUtil.buildLongOrClause("KTRowId", notasfiscaisList.size());

        for (final FormNotafiscalRowIdSTY notafiscalSTY : notasfiscaisList) {
            final int currentKtRowId = notafiscalSTY.getKtFormRowId();
            final QueryIntParameter ktRowIdParam = new QueryIntParameter(currentKtRowId);
            queryParameters.add(ktRowIdParam);
        }


    }// constructor

    @Override
    public String getPreparedUpdateQueryString() {

        final String tableName = DBDTables.TABLE_NAME_NOTASFISCAIS;

        final String queryStr =
                "UPDATE " + tableName + " SET "+ flagName + "=1, "+ flagColumnDbUpdateTime + "=SYSUTCDATETIME() WHERE " + rowIdsOrClause;

        return queryStr;
    }

    @Override
    public List<QueryParameter> getUpdateQueryParameters() {
        return queryParameters;
    }

}// class
