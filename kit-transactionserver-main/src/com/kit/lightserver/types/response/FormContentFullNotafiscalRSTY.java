package com.kit.lightserver.types.response;

import com.kit.lightserver.domain.types.NotafiscalSTY;

public final class FormContentFullNotafiscalRSTY extends FormContentFullRSTY {

    private final NotafiscalSTY notafiscalSTY;

    public FormContentFullNotafiscalRSTY(final NotafiscalSTY conhecimentoSTY) {
        this.notafiscalSTY = conhecimentoSTY;
    }// constructor

    public NotafiscalSTY getNotafiscalSTY() {
        return notafiscalSTY;
    }

    @Override
    public String toString() {
        return "FormContentFullNotafiscalRSTY [notafiscalSTY=" + notafiscalSTY + "]";
    }

}// class
