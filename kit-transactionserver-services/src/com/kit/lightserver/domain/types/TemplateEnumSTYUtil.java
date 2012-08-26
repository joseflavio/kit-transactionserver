package com.kit.lightserver.domain.types;

public final class TemplateEnumSTYUtil {

    static public String getFormTypeCodeForDatabase(final TemplateEnumSTY template) {
        if( TemplateEnumSTY.CO.equals(template) == true ) {
            return "CO";
        }
        if( TemplateEnumSTY.NF.equals(template) == true ) {
            return "NF";
        }
        throw new RuntimeException("Unexpected. template=" + template);
    }

}// class
