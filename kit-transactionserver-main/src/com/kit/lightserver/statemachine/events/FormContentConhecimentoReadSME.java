package com.kit.lightserver.statemachine.events;

import com.kit.lightserver.domain.types.FormFirstReadDateSTY;
import com.kit.lightserver.domain.types.FormClientRowIdSTY;
import com.kit.lightserver.statemachine.states.KitEventSME;

public final class FormContentConhecimentoReadSME implements KitEventSME {

    private final FormClientRowIdSTY conhecimentoId;

    private final FormFirstReadDateSTY firstReadDate;

    public FormContentConhecimentoReadSME(final FormClientRowIdSTY conhecimentoId, final FormFirstReadDateSTY firstReadDate) {
        this.conhecimentoId = conhecimentoId;
        this.firstReadDate = firstReadDate;
    }

    public FormClientRowIdSTY getClientRowId() {
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
