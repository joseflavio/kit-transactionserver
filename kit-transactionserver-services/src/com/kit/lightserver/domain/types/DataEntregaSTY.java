package com.kit.lightserver.domain.types;

import java.util.Date;

public class DataEntregaSTY {

    private final Origin origin;
    private final String dataEntregaStr;
    private final Date dataEntregaDate;

    public DataEntregaSTY(final Origin origin, final String dataEntregaStr, final Date dataEntregaDate) {
        this.origin = origin;
        this.dataEntregaStr = dataEntregaStr;
        this.dataEntregaDate = dataEntregaDate;
    }

    public enum Origin {
        FORM_FIELD, LAST_EDIT
    }

    @Override
    public String toString() {
        return "DataEntregaSTY [origin=" + origin + ", dataEntregaStr=" + dataEntregaStr + ", dataEntregaDate=" + dataEntregaDate + "]";
    }

}// class
