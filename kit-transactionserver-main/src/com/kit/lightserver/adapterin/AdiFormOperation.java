package com.kit.lightserver.adapterin;


import kit.primitives.forms.FormOperation;

import com.kit.lightserver.statemachine.events.FormOperationClientSuccessEventSME;
import com.kit.lightserver.statemachine.events.FormOperationUpdateFormsCompleteEventSME;
import com.kit.lightserver.statemachine.states.KitEventSME;

final class AdiFormOperation {

    static public ReceivedPrimitiveConverterResult<KitEventSME> adapt(final FormOperation primitive) {

        ReceivedPrimitiveConverterResult<KitEventSME> result;
        if (primitive.type == FormOperation.CLIENT_SUCCESS) {
            FormOperationClientSuccessEventSME event = new FormOperationClientSuccessEventSME();
            result = new ReceivedPrimitiveConverterResult<KitEventSME>(true, event);
        }
        else if (primitive.type == FormOperation.UPDATED_FORMS_COMPLETE) {
            FormOperationUpdateFormsCompleteEventSME event = new FormOperationUpdateFormsCompleteEventSME();
            result = new ReceivedPrimitiveConverterResult<KitEventSME>(true, event);
        }
        else {

            result = new ReceivedPrimitiveConverterResult<KitEventSME>();
        }

        return result;

    }

}// class
