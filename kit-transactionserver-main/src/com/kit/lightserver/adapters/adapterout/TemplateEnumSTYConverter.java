package com.kit.lightserver.adapters.adapterout;

import com.kit.lightserver.domain.types.TemplateEnumSTY;

final class TemplateEnumSTYConverter {

    static private final String CONHECIMENTOS_STRING = "conhecimentos";

    static private final String NOTAS_FISCAIS_STRING = "notasFiscais";

    static String convertToClientString(final TemplateEnumSTY formType) {
        final String result;
        if( TemplateEnumSTY.CO.equals(formType) ) {
            result = CONHECIMENTOS_STRING;
        }
        else if (TemplateEnumSTY.NF.equals(formType) ) {
            result = NOTAS_FISCAIS_STRING;
        }
        else {
            throw new RuntimeException("Unable to convert. templateEnumSTY="+formType);
        }
        return result;
    }

    static TemplateEnumSTY getByClientTemplateIdStr(final String value) {
        if( CONHECIMENTOS_STRING.equals(value) ) {
            return TemplateEnumSTY.CO;
        }
        if( NOTAS_FISCAIS_STRING.equals(value) ) {
            return TemplateEnumSTY.NF;
        }
        throw new RuntimeException("Unable to convert. value="+value);
    }

}// class
