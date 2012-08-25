package com.kit.lightserver.adapters.adapterin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import kit.primitives.forms.FieldAndContentBean;
import kit.primitives.forms.FormContent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fap.collections.TransformFilter;

import com.kit.lightserver.domain.types.DataEntregaSTY;
import com.kit.lightserver.domain.types.FormClientRowIdSTY;
import com.kit.lightserver.domain.types.FormFirstReadDateSTY;
import com.kit.lightserver.domain.types.StatusEntregaEnumSTY;
import com.kit.lightserver.domain.types.TemplateEnumSTY;
import com.kit.lightserver.statemachine.events.FormContentConhecimentoReadSME;
import com.kit.lightserver.statemachine.events.FormContentEditedSME;
import com.kit.lightserver.statemachine.states.KitEventSME;


final class AdiFormContent {

    static private final String CONHECIMENTOS_TYPE = "conhecimentos";
    static private final String NOTAFISCAIS_TYPE = "notasFiscais";

	static private final Logger LOGGER = LoggerFactory.getLogger(AdiFormContent.class);

	static public ReceivedPrimitiveConverterResult<KitEventSME> adapt(final FormContent primitive) {

	    final ReceivedPrimitiveConverterResult<KitEventSME> result;
		if(primitive.formStatus == FormContent.FORM_READ) {

		    FormClientRowIdSTY formId = AdiFormContent.convertFormId( primitive.formId ); // formId=conhecimentos%1094966
	        if( formId == null ) {
	            result = new ReceivedPrimitiveConverterResult<KitEventSME>();
	        }
	        else {
		        FormFirstReadDateSTY formFirstReadDate = new FormFirstReadDateSTY( primitive.firstReadDate );
	            FormContentConhecimentoReadSME event = new FormContentConhecimentoReadSME(formId, formFirstReadDate);
	            result = new ReceivedPrimitiveConverterResult<KitEventSME>(true, event);
		    }

		}
		else if(primitive.formStatus == FormContent.FORM_EDITED) {

		    final FormClientRowIdSTY formId = AdiFormContent.convertFormId( primitive.formId );
		    if( formId == null ) {
                result = new ReceivedPrimitiveConverterResult<KitEventSME>();
            }
		    else {

		        Date lastEditDate = primitive.lastEditDate;
		        AdiFieldResult<String> dataEntregaField = AdiFormContent.getFieldByName(primitive, "dataEntrega", new StringFieldConverter());

		        DataEntregaSTY dataEntrega = null;
		        String dataEntregaStr = dataEntregaField.getValue();
		        if( dataEntregaField.isExists() == true && dataEntregaStr != null ) {
                    try {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); // dataEntregaStr=25/5/2012 6:6:0, value=14/3/2012 8:50:00
                        formatter.setTimeZone( TimeZone.getTimeZone("America/Sao_Paulo") );
                        Date dataEntregaDate = formatter.parse(dataEntregaStr);
                        dataEntrega = new DataEntregaSTY(DataEntregaSTY.Origin.FORM_FIELD, dataEntregaStr, dataEntregaDate, lastEditDate);
                    }
                    catch (ParseException e) {
                        LOGGER.error("Invalid format for date. dataEntregaStr={}", dataEntregaStr, e);
                    }
		        }

		        if( dataEntrega == null && lastEditDate != null ) {
		            dataEntrega = new DataEntregaSTY(DataEntregaSTY.Origin.LAST_EDIT, dataEntregaStr, lastEditDate, lastEditDate);
		        }

		        AdiFieldResult<StatusEntregaEnumSTY> statusEntregaField =
		                AdiFormContent.getFieldByName(primitive, "statusEntrega", new StatusEntregaEnumFieldConverter());

		        if( dataEntrega == null ) {
		            LOGGER.error("Invalid dataEntrega. dataEntrega=null, dataEntregaStr={}, lastEditDate={}", dataEntregaStr, lastEditDate);
		            result = new ReceivedPrimitiveConverterResult<KitEventSME>();
		        }
		        else if( statusEntregaField.isExists() == false || statusEntregaField.getValue() == null ) {
		            LOGGER.error("Invalid statusEntregaField. statusEntregaField={}", statusEntregaField);
		            result = new ReceivedPrimitiveConverterResult<KitEventSME>();
		        }
		        else {
		            FormContentEditedSME editedSME = new FormContentEditedSME(formId, lastEditDate, statusEntregaField.getValue(), dataEntrega);
		            result = new ReceivedPrimitiveConverterResult<KitEventSME>(true, editedSME);
		        }

            }

		}
		else {
			LOGGER.error("Unexpected. primitive={}", primitive);
			result = new ReceivedPrimitiveConverterResult<KitEventSME>();
		}

		return result;

	}

	static private FormClientRowIdSTY convertFormId(final String rawFormId) {

	    String[] formIdArray = rawFormId.split("%");
        int ktRowId = Integer.parseInt( formIdArray[1] );
        String formType = formIdArray[0];

	    if( CONHECIMENTOS_TYPE.equals(formType) ) {
            final FormClientRowIdSTY conhecimentoId = new FormClientRowIdSTY(TemplateEnumSTY.KNOWLEDGE_CONHECIMENTO, ktRowId);
            return conhecimentoId;
        }
	    else if( NOTAFISCAIS_TYPE.equals(formType) ) {
	        final FormClientRowIdSTY nfId = new FormClientRowIdSTY(TemplateEnumSTY.RECEIPT_NOTASFISCAIS, ktRowId);
	        return nfId;
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
            if( input == null ) {
                return null;
            }
            if( input.equals("null")) {
                return null;
            }
            return input;
        }
	}// class

	final static class StatusEntregaEnumFieldConverter implements TransformFilter<StatusEntregaEnumSTY, String> {

        @Override
        public StatusEntregaEnumSTY transform(final String input) {
            try {
            return StatusEntregaEnumSTY.valueOf(input);
            } catch(Exception e) {
                LOGGER.error("Unexpected vule for StatusEntregaEnum. input={}", input);
                return null;
            }
        }
	}// class

}// class
