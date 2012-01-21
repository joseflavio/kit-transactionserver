package com.kit.lightserver.services.db.conhecimentos;

import com.kit.lightserver.domain.types.StatusEntregaEnumSTY;

final public class StatusEntregaSTYParser {

    static public StatusEntregaEnumSTY parse(final String value) {
        if ("AN".equals(value)) {
            return StatusEntregaEnumSTY.AN_AINDA_NAO_ENTREGUE;
        }

        throw new RuntimeException("Unable to parse. value=" + value);
    }

}// class
