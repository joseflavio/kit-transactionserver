package com.kit.lightserver.statemachine.events;

import java.util.Date;

import com.kit.lightserver.domain.types.DataEntregaSTY;
import com.kit.lightserver.domain.types.FormClientRowIdSTY;
import com.kit.lightserver.domain.types.StatusEntregaEnumSTY;
import com.kit.lightserver.statemachine.states.KitEventSME;

public final class FormContentEditedSME implements KitEventSME {

    private final FormClientRowIdSTY conhecimentoId;

    private final StatusEntregaEnumSTY statusEntregaEnumSTY;

    private final DataEntregaSTY dataEntrega;

    public FormContentEditedSME(final FormClientRowIdSTY conhecimentoId, final Date lastEditDate, final StatusEntregaEnumSTY statusEntregaEnumSTY,
            final DataEntregaSTY dataEntrega) {

        this.conhecimentoId = conhecimentoId;
        this.statusEntregaEnumSTY = statusEntregaEnumSTY;
        this.dataEntrega = dataEntrega;

    }

    public FormClientRowIdSTY getConhecimentoId() {
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
