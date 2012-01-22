package com.kit.lightserver.adapterin;

import java.util.Date;

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

		    String formId = primitive.formId; // formId=conhecimentos%1094966
		    LOGGER.error("formId="+formId);

			Date firstReadDate = primitive.firstReadDate;

			FormContentFormReadSME event = new FormContentFormReadSME(firstReadDate);
			result = new ReceivedPrimitiveConverterResult<KitEventSME>(true, event);
		}
		else {
			LOGGER.error("Unknow type. primitive="+primitive);
			result = new ReceivedPrimitiveConverterResult<KitEventSME>();
		}

		return result;

	}

}// class
