package com.kit.lightserver.adapters.adapterin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public final class TestTimeZone {

    public static void main(final String[] args) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); // dataEntregaStr=25/5/2012 6:6:0, value=14/3/2012 8:50:00
        formatter.setTimeZone( TimeZone.getTimeZone("America/Sao_Paulo") );
        Date dataEntregaDate = formatter.parse("29/06/2012 1:0:0");
        System.out.println("dataEntregaDate="+dataEntregaDate);
    }

}
