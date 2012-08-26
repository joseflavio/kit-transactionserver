package com.kit.lightserver.adapters.adapterout;

import java.util.Date;

import kit.primitives.forms.FieldAndContentBean;
import kit.primitives.forms.FormContent;
import kit.primitives.forms.FormContentFull;

import com.kit.lightserver.domain.types.ConhecimentoRSTY;
import com.kit.lightserver.domain.types.FormFlagsSTY;
import com.kit.lightserver.domain.types.NotafiscalRSTY;
import com.kit.lightserver.types.response.FormContentFullConhecimentoRSTY;
import com.kit.lightserver.types.response.FormContentFullNotafiscalRSTY;
import com.kit.lightserver.types.response.FormContentFullRSTY;

public final class FormContentFullConverter {

    //static private final Logger LOGGER = LoggerFactory.getLogger(FormContentFullConverter.class);

    static public ConverterResult convertForm(final FormContentFullRSTY formContentFullRSTY) {

        final FormContentFull formContentFull;
        if (formContentFullRSTY instanceof FormContentFullNotafiscalRSTY) {
            final FormContentFullNotafiscalRSTY forContentFullNotafiscalRSTY = (FormContentFullNotafiscalRSTY) formContentFullRSTY;
            formContentFull = FormContentFullConverter.convertNotaFiscal(forContentFullNotafiscalRSTY);
        } else if (formContentFullRSTY instanceof FormContentFullConhecimentoRSTY) {
            final FormContentFullConhecimentoRSTY forContentFullNotafiscalRSTY = (FormContentFullConhecimentoRSTY) formContentFullRSTY;
            formContentFull = FormContentFullConverter.convertConhecimento(forContentFullNotafiscalRSTY);
        } else {
            formContentFull = null;
        }

        final ConverterResult result;
        if (formContentFull != null) {
            result = new ConverterResult(true, formContentFull);
        } else {
            result = new ConverterResult();
        }

        return result;

    }

    static private FormContentFull convertConhecimento(final FormContentFullConhecimentoRSTY formContentFullConhecimentoRSTY) {

        final ConhecimentoRSTY form = formContentFullConhecimentoRSTY.getConhecimentoSTY();

        final int ktRowId = form.getFormClientRowId().getKtFormRowId();
        final String anchorCategoryId = ktRowId + "P";

        final String templateStr = TemplateEnumSTYConverter.convertToClientString( form.getFormType() );

        final FormContentFull response = new FormContentFull();
        response.formId = templateStr + "%" + ktRowId;
        response.formStatus = FormContentFullConverter.calculateFormStatus( form.getFormFlags() );
        response.templateId = templateStr;
        response.category = anchorCategoryId;
        response.title = form.getTitle();
        response.showFlags = 32; // because it is "Conhecimento" template
        response.firstReadDate = new Date(); // new Date(0L) didn't work, need to discuss
        response.lastEditDate = new Date(); // new Date(0L);

        final String childrenCategoryAncora = ktRowId + "";
        final FieldAndContentBean numeroConhecimentoBean = new FieldAndContentBean("ancora", childrenCategoryAncora);
        response.add(numeroConhecimentoBean);

        //final String statusEntregaStr = StatusEntregaConverter.toKeyString(form.getStatusEntrega());
        //final FieldAndContentBean statusEntregaBean = new FieldAndContentBean("statusEntrega", statusEntregaStr);
        //response.add(statusEntregaBean);

        final FieldAndContentBean dataEntregaBean = new FieldAndContentBean("dataEntrega", "");  // Mandatory, the mobile crashes without it
        response.add(dataEntregaBean);

        final String remetenteCNPJ = form.getRemetenteCNPJ();
        final FieldAndContentBean remetenteCNPJBean = new FieldAndContentBean("remetenteCNPJ", remetenteCNPJ);
        response.add(remetenteCNPJBean);

        final String destinatarioNome = form.getDestinatarioNome();
        final FieldAndContentBean destinatarioNomeBean = new FieldAndContentBean("destinatarioNome", destinatarioNome);
        response.add(destinatarioNomeBean);

        return response;

    }

    static public FormContentFull convertNotaFiscal(final FormContentFullNotafiscalRSTY givenFormRSTY) {

        final NotafiscalRSTY form = givenFormRSTY.getNotafiscalSTY();

        final FormContentFull response = new FormContentFull();

        final String templateStr = TemplateEnumSTYConverter.convertToClientString( form.getFormType() );
        final int ktFormRowId = form.getFormClientRowId().getKtFormRowId();
        response.formId = templateStr + "%" + ktFormRowId;
        response.formStatus = FormContentFullConverter.calculateFormStatus( form.getFormFlagsSTY() );
        response.templateId = templateStr;

        final int parentClientRowId = form.getParentFormClientKtRowId().getKtFormRowId();
        final String category = Integer.toString( parentClientRowId );
        response.category = category;

        response.title = form.getTitle();
        response.showFlags = 0; // because it is "NotaFiscal" template
        response.firstReadDate = new Date();
        response.lastEditDate = new Date();

        final String parentCategoryAncora = parentClientRowId + "P";
        final FieldAndContentBean numeroConhecimentoBean = new FieldAndContentBean("ancora", parentCategoryAncora);
        response.add(numeroConhecimentoBean);

        final FieldAndContentBean statusEntregaBean = new FieldAndContentBean("statusEntrega", "AN"); // AN = Ainda não entregue
        response.add(statusEntregaBean);

        final String dataEntregaStr = ""; //DataEntregaConverter.convertToClientString(form.getDataEntrega()); // Mandatory, the mobile crashes without it?
        final FieldAndContentBean dataEntregaBean = new FieldAndContentBean("dataEntrega", dataEntregaStr);
        response.add(dataEntregaBean);

        return response;

    }

    static public String convertToClientString(final Date date) {
        if( date == null ) {
            return ""; // The primitive date string should not be null but an empty string
        }
        return Long.toString( date.getTime() );
    }

    static private byte calculateFormStatus(final FormFlagsSTY formFlags) {
        if (formFlags.isEdited()) {
            return FormContent.FORM_EDITED;
        }
        else if (formFlags.isRead()) {
            return FormContent.FORM_READ;
        }
        else if (formFlags.isReceived()) {
            return FormContent.FORM_RECEIVED;
        }
        else {
            return FormContent.FORM_NEW;
        }
    }

}// class
