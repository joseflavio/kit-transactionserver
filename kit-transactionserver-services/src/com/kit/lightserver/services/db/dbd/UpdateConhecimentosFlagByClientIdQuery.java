package com.kit.lightserver.services.db.dbd;

import java.util.LinkedList;
import java.util.List;

import org.dajo.framework.db.QueryIntParameter;
import org.dajo.framework.db.QueryParameter;
import org.dajo.framework.db.QueryStringParameter;
import org.dajo.framework.db.UpdateQueryInterface;
import org.dajo.framework.db.util.QueryUtil;

import com.kit.lightserver.domain.types.FormClientRowIdSTY;

final class UpdateConhecimentosFlagByClientIdQuery implements UpdateQueryInterface {

    private final String flagColumn;

    private final String flagColumnDbUpdateTime;

    private final String rowIdsOrClause;

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    public UpdateConhecimentosFlagByClientIdQuery(final FormFlagEnum flag, final String ktClientUserId, final List<FormClientRowIdSTY> conhecimentosList) {

        if( conhecimentosList == null || conhecimentosList.size() == 0 ) {
            throw new RuntimeException("conhecimentosList can not be empty.");
        }

        this.flagColumn = flag.getDbFlagColumnName();
        this.flagColumnDbUpdateTime = flag.getDbFlagColumnName() + "UpdateDBTime";

        this.rowIdsOrClause = QueryUtil.buildLongOrClause("KTRowId", conhecimentosList.size());

        final QueryStringParameter ktClientIdParam = new QueryStringParameter(ktClientUserId);
        queryParameters.add(ktClientIdParam);

        for (final FormClientRowIdSTY clientRowIdSTY : conhecimentosList) {
            assert( FormClientRowIdSTY.isConhecimento(clientRowIdSTY) == true );
            int currentId = clientRowIdSTY.getKtFormRowId();
            QueryIntParameter ktRowIdParam = new QueryIntParameter(currentId);
            queryParameters.add(ktRowIdParam);
        }

    }// constructor

    @Override
    public String getPreparedUpdateQueryString() {

        final String queryStr =
                "UPDATE " + DBDTables.CONHECIMENTOS.TABLE_NAME +
                " SET "+ flagColumn + "=1, "+ flagColumnDbUpdateTime + "=SYSUTCDATETIME() WHERE KTClientUserId=? AND " + rowIdsOrClause;

        return queryStr;
    }

    @Override
    public List<QueryParameter> getUpdateQueryParameters() {
        return queryParameters;
    }

}// class
