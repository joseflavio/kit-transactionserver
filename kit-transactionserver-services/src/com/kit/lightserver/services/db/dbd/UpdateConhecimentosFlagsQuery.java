package com.kit.lightserver.services.db.dbd;

import java.util.LinkedList;
import java.util.List;

import org.dajo.framework.db.QueryIntParameter;
import org.dajo.framework.db.QueryParameter;
import org.dajo.framework.db.QueryStringParameter;
import org.dajo.framework.db.UpdateQueryInterface;
import org.dajo.framework.db.util.QueryUtil;

import com.kit.lightserver.domain.types.FormConhecimentoRowIdSTY;

final class UpdateConhecimentosFlagsQuery implements UpdateQueryInterface {

    private final String flagColumn;

    private final String flagColumnDbUpdateTime;

    private final String rowIdsOrClause;

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    public UpdateConhecimentosFlagsQuery(final FormFlagEnum flag, final String ktClientUserId, final List<FormConhecimentoRowIdSTY> conhecimentosList) {

        if( conhecimentosList == null || conhecimentosList.size() == 0 ) {
            throw new RuntimeException("conhecimentosList can not be empty.");
        }

        this.flagColumn = flag.getDbFlagColumnName();
        this.flagColumnDbUpdateTime = flag.getDbFlagColumnName() + "UpdateDBTime";

        this.rowIdsOrClause = QueryUtil.buildLongOrClause("KTRowId", conhecimentosList.size());

        final QueryStringParameter ktClientIdParam = new QueryStringParameter(ktClientUserId);
        queryParameters.add(ktClientIdParam);

        for (final FormConhecimentoRowIdSTY conhecimentoSTY : conhecimentosList) {
            final int currentKtRowId = conhecimentoSTY.getKtFormRowId();
            final QueryIntParameter ktRowIdParam = new QueryIntParameter(currentKtRowId);
            queryParameters.add(ktRowIdParam);
        }

    }// constructor

    @Override
    public String getPreparedUpdateQueryString() {

        final String tableName = DBDTables.TABLE_NAME_CONHECIMENTOS;

        final String queryStr =
                "UPDATE " + tableName + " SET "+ flagColumn + "=1, "+ flagColumnDbUpdateTime + "=SYSUTCDATETIME() WHERE KTClientUserId=? AND " + rowIdsOrClause;

        return queryStr;
    }

    @Override
    public List<QueryParameter> getUpdateQueryParameters() {
        return queryParameters;
    }

}// class