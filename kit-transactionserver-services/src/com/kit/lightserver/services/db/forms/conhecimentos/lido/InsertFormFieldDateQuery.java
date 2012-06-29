package com.kit.lightserver.services.db.forms.conhecimentos.lido;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.dajo.framework.db.InsertQueryInterface;
import org.dajo.framework.db.QueryDateParameter;
import org.dajo.framework.db.QueryIntParameter;
import org.dajo.framework.db.QueryParameter;
import org.dajo.framework.db.QueryStringParameter;

import com.kit.lightserver.domain.types.FormRowIdSTY;
import com.kit.lightserver.domain.types.FormTypeEnumSTY;
import com.kit.lightserver.services.db.forms.conhecimentos.TableConhecimentosConstants;

public final class InsertFormFieldDateQuery implements InsertQueryInterface {

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    public InsertFormFieldDateQuery(final FormTypeEnumSTY formType, final FormRowIdSTY formRowId, final String formFieldName,
            final Date formFieldValue, final String formFieldDebug) {

        final QueryStringParameter formTypeParam = new QueryStringParameter(formType.getFormTypeCodeForDatabase());
        queryParameters.add(formTypeParam);

        final int formRowIdInt = formRowId.getKtFormRowId();
        final QueryIntParameter formRowIdParam = new QueryIntParameter(formRowIdInt);
        queryParameters.add(formRowIdParam);

        final QueryStringParameter formFieldNameParam = new QueryStringParameter(formFieldName);
        queryParameters.add(formFieldNameParam);

        final QueryDateParameter formFieldValueParam = new QueryDateParameter(formFieldValue);
        queryParameters.add(formFieldValueParam);

        final QueryStringParameter formFieldDebugParam = new QueryStringParameter(formFieldDebug);
        queryParameters.add(formFieldDebugParam);

    }// constructor

    @Override
    public String getPreparedInsertQueryString() {

        final String queryStr = "INSERT INTO " + TableConhecimentosConstants.TABLE_FORM_FIELD_DATE
                + " ( [KTFormType], [KTFormRowId], [KTFormFieldName], [KTFormFieldValue], [KTFormFieldDebug] ) VALUES ( ?, ?, ?, ?, ? )";

        return queryStr;

    }

    @Override
    public List<QueryParameter> getInsertQueryParameters() {
        return queryParameters;
    }

}// class
