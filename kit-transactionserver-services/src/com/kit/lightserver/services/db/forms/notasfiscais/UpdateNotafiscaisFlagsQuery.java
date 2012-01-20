package com.kit.lightserver.services.db.forms.notasfiscais;

import java.util.LinkedList;
import java.util.List;

import com.kit.lightserver.domain.NotafiscalSTY;
import com.kit.lightserver.services.db.QueryIntegerParameter;
import com.kit.lightserver.services.db.QueryParameter;
import com.kit.lightserver.services.db.UpdateQueryInterface;
import com.kit.lightserver.services.db.common.QueryUtil;

public final class UpdateNotafiscaisFlagsQuery implements UpdateQueryInterface {

    private final String flagName;

    private final String rowIdsOrClause;

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    public UpdateNotafiscaisFlagsQuery(final String flagName, final List<NotafiscalSTY> notasfiscaisList) {

        if( notasfiscaisList == null || notasfiscaisList.size() == 0 ) {
            throw new RuntimeException("conhecimentosList can not be empty.");
        }

        this.flagName = flagName;

        this.rowIdsOrClause = QueryUtil.buildLongOrClause("KTRowId", notasfiscaisList.size());

        for (final NotafiscalSTY notafiscalSTY : notasfiscaisList) {
            final int currentKtRowId = notafiscalSTY.getKtRowId();
            final QueryIntegerParameter ktRowIdParam = new QueryIntegerParameter(currentKtRowId);
            queryParameters.add(ktRowIdParam);
        }


    }// constructor

    @Override
    public String getPreparedUpdateQueryString() {

        final String tableName = TableNotasfiscaisConstants.TABLE_NAME_NOTASFISCAIS;
        final String flagColumn = "KTFlag" + flagName;
        final String flagColumnDbUpdateTime = "KTFlag" + flagName + "UpdateDBTime";

        final String queryStr =
                "UPDATE " + tableName + " SET "+ flagColumn + "=1, "+ flagColumnDbUpdateTime + "=GETDATE() WHERE " + rowIdsOrClause;

        return queryStr;
    }

    @Override
    public List<QueryParameter> getUpdateQueryParameters() {
        return queryParameters;
    }

}// class
