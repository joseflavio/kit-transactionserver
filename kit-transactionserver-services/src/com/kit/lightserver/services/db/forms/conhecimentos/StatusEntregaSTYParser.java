package com.kit.lightserver.services.db.forms.conhecimentos;

import com.kit.lightserver.domain.types.StatusEntregaEnumSTY;

final public class StatusEntregaSTYParser {

    static public StatusEntregaEnumSTY parse(final String value) {
        if ("AN".equals(value)) {
            return StatusEntregaEnumSTY.AN;
        }

        throw new RuntimeException("Unable to parse. value=" + value);
    }

}// class
