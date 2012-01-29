package com.kit.lightserver.adapters.adapterout;

import kit.primitives.forms.FormOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kit.lightserver.types.response.FormOperationGetStatusRSTY;
import com.kit.lightserver.types.response.FormOperationRSTY;
import com.kit.lightserver.types.response.FormOperationResetRSTY;
import com.kit.lightserver.types.response.FormOperationUpdatedFormsClearFlagsRSTY;
import com.kit.lightserver.types.response.FormOperationUpdatedFormsRequestRSTY;

public final class AdoFormOperationConverter {

    static private final Logger LOGGER = LoggerFactory.getLogger(AdoFormOperationConverter.class);

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
            LOGGER.error("Invalid Form Operation.");
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
