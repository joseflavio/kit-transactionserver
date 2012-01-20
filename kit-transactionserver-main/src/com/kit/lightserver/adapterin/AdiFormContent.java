package com.kit.lightserver.adapterin;

import kit.primitives.forms.FormContent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kit.lightserver.statemachine.events.FormContentFormReadSME;
import com.kit.lightserver.statemachine.states.KitEventSME;


final class AdiFormContent {

	static private final Logger LOGGER = LoggerFactory.getLogger(AdiFormContent.class);

	static public ReceivedPrimitiveConverterResult<KitEventSME> adapt(final FormContent primitive) {

	    final ReceivedPrimitiveConverterResult<KitEventSME> result;
		if(primitive.formStatus == FormContent.FORM_READ) {
			FormContentFormReadSME event = new FormContentFormReadSME();
			result = new ReceivedPrimitiveConverterResult<KitEventSME>(true, event);
		}
		else {
			LOGGER.error("Unknow type. primitive="+primitive);
			result = new ReceivedPrimitiveConverterResult<KitEventSME>();
		}

		return result;

	}

}// class
