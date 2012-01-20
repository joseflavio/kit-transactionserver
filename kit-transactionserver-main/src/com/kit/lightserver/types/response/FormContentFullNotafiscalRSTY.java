package com.kit.lightserver.types.response;

import com.kit.lightserver.domain.NotafiscalSTY;

public final class FormContentFullNotafiscalRSTY extends FormContentFullRSTY implements ClientResponseRSTY {

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
