package com.kit.lightserver.adapterout;

import kit.primitives.forms.FormOperation;

import com.kit.lightserver.types.response.FormOperationGetStatusRSTY;
import com.kit.lightserver.types.response.FormOperationRSTY;
import com.kit.lightserver.types.response.FormOperationResetRSTY;
import com.kit.lightserver.types.response.FormOperationUpdatedFormsClearFlagsRSTY;
import com.kit.lightserver.types.response.FormOperationUpdatedFormsRequestRSTY;

public final class FormOperationConverter {

    static public ConverterResult converter(final FormOperationRSTY clientResponseRSTY) {

        final FormOperation formOperation;
        if (clientResponseRSTY instanceof FormOperationUpdatedFormsClearFlagsRSTY) {
            formOperation = new FormOperation();
            formOperation.type = FormOperation.UPDATED_FORMS_CLEAR_FLAGS;
        } else if (clientResponseRSTY instanceof FormOperationUpdatedFormsRequestRSTY) {
            formOperation = new FormOperation();
            formOperation.type = FormOperation.UPDATED_FORMS;
        } else if (clientResponseRSTY instanceof FormOperationResetRSTY) {
            formOperation = new FormOperation();
            formOperation.type = FormOperation.RESET;
        } else if (clientResponseRSTY instanceof FormOperationGetStatusRSTY) {
            formOperation = new FormOperation();
            formOperation.type = FormOperation.GET_STATUS;
        } else {
            formOperation = null;
        }

        final ConverterResult result;
        if (formOperation != null) {
            result = new ConverterResult(true, formOperation);
        } else {
            result = new ConverterResult();
        }

        return result;

    }

}// class
