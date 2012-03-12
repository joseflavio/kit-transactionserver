package com.kit.lightserver.adapters.adapterin;

import java.util.Date;

import kit.primitives.forms.FieldAndContentBean;
import kit.primitives.forms.FormContent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kit.lightserver.domain.types.ConhecimentoIdSTY;
import com.kit.lightserver.statemachine.events.FormContentConhecimentoReadSME;
import com.kit.lightserver.statemachine.states.KitEventSME;


final class AdiFormContent {

    static private final String CONHECIMENTOS_TYPE = "conhecimentos";

	static private final Logger LOGGER = LoggerFactory.getLogger(AdiFormContent.class);

	static public ReceivedPrimitiveConverterResult<KitEventSME> adapt(final FormContent primitive) {

	    final ReceivedPrimitiveConverterResult<KitEventSME> result;
		if(primitive.formStatus == FormContent.FORM_READ) {

		    ConhecimentoIdSTY conhecimentoId = AdiFormContent.convertFormId( primitive.formId ); // formId=conhecimentos%1094966
		    if( conhecimentoId == null ) {
		        result = new ReceivedPrimitiveConverterResult<KitEventSME>();
		    }
		    else {
		        Date firstReadDate = primitive.firstReadDate;
	            FormContentConhecimentoReadSME event = new FormContentConhecimentoReadSME(conhecimentoId, firstReadDate);
	            result = new ReceivedPrimitiveConverterResult<KitEventSME>(true, event);
		    }

		}
		else if(primitive.formStatus == FormContent.FORM_EDITED) {

		    ConhecimentoIdSTY conhecimentoId = AdiFormContent.convertFormId( primitive.formId );
		    if( conhecimentoId == null ) {
                result = new ReceivedPrimitiveConverterResult<KitEventSME>();
            }
		    else {
		        Date lastEditDate = primitive.lastEditDate;
		        String statusEntrega = AdiFormContent.getFieldByName(primitive, "statusEntrega");
		        String dataEntrega = AdiFormContent.getFieldByName(primitive, "dataEntrega");
		        LOGGER.error("lastEditDate={}", lastEditDate);
		        LOGGER.error("statusEntrega={}", statusEntrega);
		        if( dataEntrega == null ) {
		            dataEntrega = "blah";
		        }
		        else {
		            dataEntrega = "*" + dataEntrega + "*";
		        }
		        LOGGER.error("dataEntrega={}", dataEntrega);
	            result = new ReceivedPrimitiveConverterResult<KitEventSME>();
		    }
		}
		else {
			LOGGER.error("Unknow type. primitive="+primitive);
			result = new ReceivedPrimitiveConverterResult<KitEventSME>();
		}

		return result;

	}

	static private ConhecimentoIdSTY convertFormId(final String rawFormId) {

	    String[] formIdArray = rawFormId.split("%");
        int ktRowId = Integer.parseInt( formIdArray[1] );
        String formType = formIdArray[0];

	    if( CONHECIMENTOS_TYPE.equals(formType) ) {
            final ConhecimentoIdSTY conhecimentoId = new ConhecimentoIdSTY(ktRowId);
            return conhecimentoId;
        }
        else {
            LOGGER.error("Invalid form type. formType={}", formType);
            LOGGER.error("splitedFormIdArray[0]="+formIdArray[0]); //splitedFormIdArray[0]=conhecimentos
            LOGGER.error("splitedFormIdArray[1]="+formIdArray[1]); //splitedFormIdArray[1]=1094966
            return null;
        }

	}

	static String getFieldByName(final FormContent primitive, final String fieldName) {
	    for(int i=0; i < primitive.size(); ++i) {
	        FieldAndContentBean current = primitive.get(i);
	        if( fieldName.equals(current.getFieldName()) ) {
	            return current.getContent();
	        }
	    }
	    return null;
	}

}// class
