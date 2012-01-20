package com.kit.lightserver.adapterout;

import com.kit.lightserver.domain.StatusEntregaEnumSTY;

final class StatusEntregaConverter {

    public static String toKeyString(final StatusEntregaEnumSTY value) {

        if( StatusEntregaEnumSTY.AN_AINDA_NAO_ENTREGUE.equals(value) ) {
            return "AN";
        }

        throw new RuntimeException("Unable to convert. value="+value);

    }

}// class
