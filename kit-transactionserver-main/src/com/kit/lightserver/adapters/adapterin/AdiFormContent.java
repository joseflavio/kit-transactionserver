package com.kit.lightserver.adapters.adapterin;

import java.util.Date;

import kit.primitives.forms.FieldAndContentBean;
import kit.primitives.forms.FormContent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fap.collections.TransformFilter;
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

		        AdiFieldResult<String> statusEntregaField = AdiFormContent.getFieldByName(primitive, "statusEntrega", new StringFieldConverter());
		        AdiFieldResult<String> dataEntregaField = AdiFormContent.getFieldByName(primitive, "dataEntrega", new StringFieldConverter());

		        LOGGER.error("lastEditDate={}", lastEditDate);
		        LOGGER.error("statusEntrega={}", statusEntregaField); // value=SU
		        LOGGER.error("dataEntrega={}", dataEntregaField); // value=14/3/2012 8:50:0

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

	static <T> AdiFieldResult<T> getFieldByName(final FormContent primitive, final String fieldName,  final TransformFilter<T, String> converter) {
	    for(int i=0; i < primitive.size(); ++i) {
	        FieldAndContentBean current = primitive.get(i);
	        if( fieldName.equals(current.getFieldName()) ) {
	            String originalValue = current.getContent();
	            T transformedValue = converter.transform(originalValue);
	            return new AdiFieldResult<T>(transformedValue);
	        }
	    }
	    return new AdiFieldResult<T>();
	}

	final static class StringFieldConverter implements TransformFilter<String, String> {
        @Override
        public String transform(final String input) {
            if( input.equals("null")) {
                return null;
            }
            return input;
        }
	}


	final static class AdiFieldResult<T> {
	    private final boolean exists;
	    private final T value;
	    public AdiFieldResult() {
	        this.exists = false;
	        this.value = null;
	    }
	    public AdiFieldResult(final T value) {
	        this.exists = true;
	        this.value = value;
	    }
	    public boolean isExists() {
	        return exists;
	    }
	    public T getValue() {
	        return value;
	    }
        @Override
        public String toString() {
            return "AdiFieldResult [exists=" + exists + ", value=" + value + "]";
        }
	}// class

}// class
