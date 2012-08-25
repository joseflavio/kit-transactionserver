package com.kit.lightserver.adapters.adapterout;

import java.util.Date;

final class DataEntregaConverter {

    static public String convertToClientString(final Date date) {
        if( date == null ) {
            return ""; // The primitive date string should not be null but an empty string
        }
        return Long.toString( date.getTime() );
    }

}// class
