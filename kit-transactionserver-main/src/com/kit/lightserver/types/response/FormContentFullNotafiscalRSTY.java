package com.kit.lightserver.types.response;

import com.kit.lightserver.domain.types.NotafiscalRSTY;

public final class FormContentFullNotafiscalRSTY extends FormContentFullRSTY {

    private final NotafiscalRSTY notafiscalSTY;

    public FormContentFullNotafiscalRSTY(final NotafiscalRSTY notafiscalSTY) {
        if( notafiscalSTY == null ) {
            throw new NullPointerException();
        }
        this.notafiscalSTY = notafiscalSTY;
    }// constructor

    public NotafiscalRSTY getNotafiscalSTY() {
        return notafiscalSTY;
    }

    @Override
    public String toString() {
        return "FormContentFullNotafiscalRSTY [notafiscalSTY=" + notafiscalSTY + "]";
    }

}// class
