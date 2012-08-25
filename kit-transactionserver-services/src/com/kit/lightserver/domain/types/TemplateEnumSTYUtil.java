package com.kit.lightserver.domain.types;

public final class TemplateEnumSTYUtil {

    static public String getFormTypeCodeForDatabase(final TemplateEnumSTY template) {
        if( TemplateEnumSTY.KNOWLEDGE_CONHECIMENTO.equals(template) == true ) {
            return "CO";
        }
        if( TemplateEnumSTY.RECEIPT_NOTASFISCAIS.equals(template) == true ) {
            return "NF";
        }
        throw new RuntimeException("Unexpected. template=" + template);
    }

}// class
