package com.kit.lightserver.services.db.forms.flags;

import java.util.LinkedList;
import java.util.List;

import org.dajo.framework.db.QueryIntParameter;
import org.dajo.framework.db.QueryParameter;
import org.dajo.framework.db.QueryStringParameter;
import org.dajo.framework.db.UpdateQueryInterface;
import org.dajo.framework.db.util.QueryUtil;

import com.kit.lightserver.domain.types.FormConhecimentoRowIdSTY;
import com.kit.lightserver.services.db.forms.conhecimentos.TableConhecimentosConstants;

final class UpdateConhecimentosFlagsQuery implements UpdateQueryInterface {

    private final String flagColumn;

    private final String flagColumnDbUpdateTime;

    private final String rowIdsOrClause;

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    public UpdateConhecimentosFlagsQuery(final FormFlagEnum flag, final String ktClientUserId, final List<FormConhecimentoRowIdSTY> conhecimentosList) {

        if( conhecimentosList == null || conhecimentosList.size() == 0 ) {
            throw new RuntimeException("conhecimentosList can not be empty.");
        }

        this.flagColumn = "KTFlag" + flag.getDatabaseColumnName();
        this.flagColumnDbUpdateTime = "KTFlag" + flag.getDatabaseColumnName() + "UpdateDBTime";

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

        final String tableName = TableConhecimentosConstants.TABLE_NAME_CONHECIMENTOS;

        final String queryStr =
                "UPDATE " + tableName + " SET "+ flagColumn + "=1, "+ flagColumnDbUpdateTime + "=GETDATE() WHERE KTClientUserId=? AND " + rowIdsOrClause;

        return queryStr;
    }

    @Override
    public List<QueryParameter> getUpdateQueryParameters() {
        return queryParameters;
    }

}// class
