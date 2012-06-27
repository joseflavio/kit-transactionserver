package com.kit.lightserver.statemachine.events;

import com.kit.lightserver.domain.types.FormConhecimentoRowIdSTY;
import com.kit.lightserver.domain.types.FormFirstReadDateSTY;
import com.kit.lightserver.statemachine.states.KitEventSME;

public final class FormContentConhecimentoReadSME implements KitEventSME {

    private final FormConhecimentoRowIdSTY conhecimentoId;

    private final FormFirstReadDateSTY firstReadDate;

    public FormContentConhecimentoReadSME(final FormConhecimentoRowIdSTY conhecimentoId, final FormFirstReadDateSTY firstReadDate) {
        this.conhecimentoId = conhecimentoId;
        this.firstReadDate = firstReadDate;
    }

    public FormConhecimentoRowIdSTY getConhecimentoId() {
        return conhecimentoId;
    }

    public FormFirstReadDateSTY getFirstReadDate() {
        return firstReadDate;
    }

    @Override
    public String toString() {
        return "FormContentConhecimentoReadSME [conhecimentoId=" + conhecimentoId + ", firstReadDate=" + firstReadDate + "]";
    }

}// class
