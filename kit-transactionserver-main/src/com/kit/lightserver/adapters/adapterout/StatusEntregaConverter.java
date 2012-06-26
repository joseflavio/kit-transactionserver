package com.kit.lightserver.adapters.adapterout;

import com.kit.lightserver.domain.types.StatusEntregaEnumSTY;

final class StatusEntregaConverter {

    public static String toKeyString(final StatusEntregaEnumSTY value) {

        if( StatusEntregaEnumSTY.AN.equals(value) ) {
            return "AN";
        }

        throw new RuntimeException("Unable to convert. value="+value);

    }

}// class
