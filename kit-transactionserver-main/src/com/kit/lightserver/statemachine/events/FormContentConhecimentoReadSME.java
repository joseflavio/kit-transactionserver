package com.kit.lightserver.statemachine.events;

import java.util.Date;

import com.kit.lightserver.domain.types.ConhecimentoIdSTY;
import com.kit.lightserver.statemachine.states.KitEventSME;

public final class FormContentConhecimentoReadSME implements KitEventSME {

    private final ConhecimentoIdSTY conhecimentoId;

    private final Date firstReadDate;

    public FormContentConhecimentoReadSME(final ConhecimentoIdSTY conhecimentoId, final Date firstReadDate) {
        this.conhecimentoId = conhecimentoId;
        this.firstReadDate = firstReadDate;
    }

    @Override
    public String toString() {
        return "FormContentConhecimentoReadSME [conhecimentoId=" + conhecimentoId + ", firstReadDate=" + firstReadDate + "]";
    }

}// class
