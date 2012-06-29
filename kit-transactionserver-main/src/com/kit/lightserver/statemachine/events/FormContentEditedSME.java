package com.kit.lightserver.statemachine.events;

import java.util.Date;

import com.kit.lightserver.domain.types.DataEntregaSTY;
import com.kit.lightserver.domain.types.FormRowIdSTY;
import com.kit.lightserver.domain.types.StatusEntregaEnumSTY;
import com.kit.lightserver.statemachine.states.KitEventSME;

public final class FormContentEditedSME implements KitEventSME {

    private final FormRowIdSTY conhecimentoId;

    private final StatusEntregaEnumSTY statusEntregaEnumSTY;

    private final DataEntregaSTY dataEntrega;

    public FormContentEditedSME(final FormRowIdSTY conhecimentoId, final Date lastEditDate, final StatusEntregaEnumSTY statusEntregaEnumSTY,
            final DataEntregaSTY dataEntrega) {

        this.conhecimentoId = conhecimentoId;
        this.statusEntregaEnumSTY = statusEntregaEnumSTY;
        this.dataEntrega = dataEntrega;

    }

    public FormRowIdSTY getConhecimentoId() {
        return conhecimentoId;
    }

    public StatusEntregaEnumSTY getStatusEntregaEnumSTY() {
        return statusEntregaEnumSTY;
    }

    public DataEntregaSTY getDataEntrega() {
        return dataEntrega;
    }

    @Override
    public String toString() {
        return "FormContentEditedSME [conhecimentoId=" + conhecimentoId + ", statusEntregaEnumSTY=" + statusEntregaEnumSTY + ", dataEntrega=" + dataEntrega
                + "]";
    }

}// class
