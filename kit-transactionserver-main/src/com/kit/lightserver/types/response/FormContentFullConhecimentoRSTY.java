package com.kit.lightserver.types.response;

import com.kit.lightserver.domain.types.ConhecimentoSTY;

public final class FormContentFullConhecimentoRSTY extends FormContentFullRSTY implements ClientResponseRSTY {

    private final ConhecimentoSTY conhecimentoSTY;

    public FormContentFullConhecimentoRSTY(final ConhecimentoSTY conhecimentoSTY) {
        this.conhecimentoSTY = conhecimentoSTY;
    }// constructor

    public ConhecimentoSTY getConhecimentoSTY() {
        return conhecimentoSTY;
    }

    @Override
    public String toString() {
        return "FormContentFullConhecimentoRSTY [conhecimentoSTY=" + conhecimentoSTY + "]";
    }

}// class
