package com.kit.lightserver.statemachine.events;

import java.util.Date;

import com.kit.lightserver.domain.types.ConhecimentoIdSTY;
import com.kit.lightserver.domain.types.DataEntregaSTY;
import com.kit.lightserver.domain.types.StatusEntregaEnumSTY;
import com.kit.lightserver.statemachine.states.KitEventSME;

public final class FormContentConhecimentoEditedSME implements KitEventSME {

    private final ConhecimentoIdSTY conhecimentoId;

    private final Date lastEditDate;

    private final StatusEntregaEnumSTY statusEntregaEnumSTY;

    private final DataEntregaSTY dataEntrega;

    public FormContentConhecimentoEditedSME(final ConhecimentoIdSTY conhecimentoId, final Date lastEditDate, final StatusEntregaEnumSTY statusEntregaEnumSTY,
            final DataEntregaSTY dataEntrega) {

        this.conhecimentoId = conhecimentoId;
        this.lastEditDate = new Date(lastEditDate.getTime());
        this.statusEntregaEnumSTY = statusEntregaEnumSTY;
        this.dataEntrega = dataEntrega;

    }

    public ConhecimentoIdSTY getConhecimentoId() {
        return conhecimentoId;
    }

    public Date getLastEditDate() {
        return new Date( lastEditDate.getTime() );
    }

    public StatusEntregaEnumSTY getStatusEntregaEnumSTY() {
        return statusEntregaEnumSTY;
    }

    public DataEntregaSTY getDataEntrega() {
        return dataEntrega;
    }

    @Override
    public String toString() {
        return "FormContentConhecimentoEditedSME [conhecimentoId=" + conhecimentoId + ", lastEditDate=" + lastEditDate + ", statusEntregaEnumSTY="
                + statusEntregaEnumSTY + ", dataEntrega=" + dataEntrega + "]";
    }

}// class
