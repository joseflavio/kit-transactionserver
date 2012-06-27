package com.kit.lightserver.domain.types;

import java.util.Date;

public class DataEntregaSTY {

    private final Origin origin;
    private final String dataEntregaStr;
    private final Date dataEntregaDate;
    private final Date lastEditDate;

    public DataEntregaSTY(final Origin origin, final String dataEntregaStr, final Date dataEntregaDate, final Date lastEditDate) {
        this.origin = origin;
        this.dataEntregaStr = dataEntregaStr;
        this.dataEntregaDate = dataEntregaDate;
        this.lastEditDate = new Date( lastEditDate.getTime() );
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
        return dataEntregaDate;
    }

    public Date getLastEditDate() {
        return lastEditDate;
    }

    @Override
    public String toString() {
        return "DataEntregaSTY [origin=" + origin + ", dataEntregaStr=" + dataEntregaStr + ", dataEntregaDate=" + dataEntregaDate + ", lastEditDate="
                + lastEditDate + "]";
    }

}// class
