package com.kit.lightserver.domain.util;

import java.util.Date;

public final class DateCopier {

    static public  Date newInstance(final Date dataEntregaDate) {
        if( dataEntregaDate == null ) {
            return null;
        }
        return new Date( dataEntregaDate.getTime() );
    }

}// class
