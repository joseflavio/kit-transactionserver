package com.kit.lightserver.types.response;

import com.kit.lightserver.domain.types.ConhecimentoRSTY;

public final class FormContentFullConhecimentoRSTY extends FormContentFullRSTY {

    private final ConhecimentoRSTY conhecimentoSTY;

    public FormContentFullConhecimentoRSTY(final ConhecimentoRSTY conhecimentoSTY) {
        if( conhecimentoSTY == null ) {
            throw new NullPointerException();
        }
        this.conhecimentoSTY = conhecimentoSTY;
    }// constructor

    public ConhecimentoRSTY getConhecimentoSTY() {
        return conhecimentoSTY;
    }

    @Override
    public String toString() {
        return "FormContentFullConhecimentoRSTY [conhecimentoSTY=" + conhecimentoSTY + "]";
    }

}// class
