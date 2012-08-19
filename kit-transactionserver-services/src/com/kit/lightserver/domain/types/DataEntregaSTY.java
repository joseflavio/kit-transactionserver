package com.kit.lightserver.domain.types;

import java.util.Date;

import com.kit.lightserver.domain.util.DateCopier;

public class DataEntregaSTY {

    private final Origin origin;
    private final String dataEntregaStr;
    private final Date dataEntregaDate;
    private final Date lastEditDate;

    public DataEntregaSTY(final Origin origin, final String dataEntregaStr, final Date dataEntregaDate, final Date lastEditDate) {
        this.origin = origin;
        this.dataEntregaStr = dataEntregaStr;
        this.dataEntregaDate = DateCopier.newInstance( dataEntregaDate );
        this.lastEditDate = DateCopier.newInstance( lastEditDate );
    }

    public enum Origin {
        FORM_FIELD, LAST_EDIT
    }

    public Origin getOrigin() {
        return origin;
    }

    public String getDataEntregaStr() {
        return dataEntregaStr;
    }

    public Date getDataEntregaDate() {
        return DateCopier.newInstance( dataEntregaDate );
    }

    public Date getLastEditDate() {
        return DateCopier.newInstance( lastEditDate );
    }

    @Override
    public String toString() {
        return "DataEntregaSTY [origin=" + origin + ", dataEntregaStr=" + dataEntregaStr + ", dataEntregaDate=" + dataEntregaDate + ", lastEditDate="
                + lastEditDate + "]";
    }

}// class
