package com.kit.lightserver.services.db.dbl;

import java.util.LinkedList;
import java.util.List;

import org.dajo.framework.db.InsertQueryInterface;
import org.dajo.framework.db.QueryIntParameter;
import org.dajo.framework.db.QueryParameter;
import org.dajo.framework.db.QueryStringParameter;

import com.kit.lightserver.domain.types.FormRowIdSTY;
import com.kit.lightserver.domain.types.FormTypeEnumSTY;

public final class InsertFormFieldString32Query implements InsertQueryInterface {

    private final List<QueryParameter> queryParameters = new LinkedList<QueryParameter>();

    public InsertFormFieldString32Query(final FormTypeEnumSTY formType, final FormRowIdSTY formRowId, final String formFieldName,
            final String formFieldValue, final String formFieldDebug) {

        final QueryStringParameter formTypeParam = new QueryStringParameter(formType.getFormTypeCodeForDatabase());
        queryParameters.add(formTypeParam);

        final int formRowIdInt = formRowId.getKtFormRowId();
        final QueryIntParameter formRowIdParam = new QueryIntParameter(formRowIdInt);
        queryParameters.add(formRowIdParam);

        final QueryStringParameter formFieldNameParam = new QueryStringParameter(formFieldName);
        queryParameters.add(formFieldNameParam);

        final QueryStringParameter formFieldValueParam = new QueryStringParameter(formFieldValue);
        queryParameters.add(formFieldValueParam);

        final QueryStringParameter formFieldDebugParam = new QueryStringParameter(formFieldDebug);
        queryParameters.add(formFieldDebugParam);

    }// constructor

    @Override
    public String getPreparedInsertQueryString() {

        final String queryStr = "INSERT INTO " + DBLTables.TABLE_FORM_FIELD_STRING32
                + " ( [KTFormType], [KTFormRowId], [KTFormFieldName], [KTFormFieldValue], [KTFormFieldDebug] ) VALUES ( ?, ?, ?, ?, ? )";

        return queryStr;

    }

    @Override
    public List<QueryParameter> getInsertQueryParameters() {
        return queryParameters;
    }

}// class
