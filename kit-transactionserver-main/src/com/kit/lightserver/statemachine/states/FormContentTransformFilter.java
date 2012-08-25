package com.kit.lightserver.statemachine.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fap.collections.SmartCollectionTransformFilter;

import com.kit.lightserver.domain.types.ConhecimentoRSTY;
import com.kit.lightserver.domain.types.ConhecimentoSTY;
import com.kit.lightserver.domain.types.FormSTY;
import com.kit.lightserver.domain.types.NotafiscalRSTY;
import com.kit.lightserver.domain.types.NotafiscalSTY;
import com.kit.lightserver.types.response.ClientResponseRSTY;
import com.kit.lightserver.types.response.FormContentFullConhecimentoRSTY;
import com.kit.lightserver.types.response.FormContentFullNotafiscalRSTY;
import com.kit.lightserver.types.response.FormContentFullRSTY;

public final class FormContentTransformFilter implements SmartCollectionTransformFilter<ClientResponseRSTY, FormSTY> {

    static private final Logger LOGGER = LoggerFactory.getLogger(FormContentTransformFilter.class);

    @Override
    public ClientResponseRSTY transform(final FormSTY formSTY) {

        final FormContentFullRSTY clientResponse;
        if (formSTY instanceof ConhecimentoSTY) {
            ConhecimentoRSTY conhecimentoForClient = buildConhecimentoForClient( (ConhecimentoSTY) formSTY );
            clientResponse = new FormContentFullConhecimentoRSTY(conhecimentoForClient);
        }
        else if (formSTY instanceof NotafiscalSTY) {
            NotafiscalRSTY notafiscalForClient = buildNotafiscalForClient( (NotafiscalSTY) formSTY );
            clientResponse = new FormContentFullNotafiscalRSTY(notafiscalForClient);
        }
        else {
            LOGGER.error("Unexpected form type. formSTY={}", formSTY);
            throw new RuntimeException("Unexpected form type. formSTY="+formSTY);
        }

        return clientResponse;

    }

    private ConhecimentoRSTY buildConhecimentoForClient(final ConhecimentoSTY formSTY) {
        ConhecimentoRSTY conhecimentoRSTY = new ConhecimentoRSTY(formSTY.getFormClientRowId(), formSTY.getFormFlags(), formSTY.getTitle(),
                formSTY.getRemetenteCNPJ(), formSTY.getDestinatarioNome());
        return conhecimentoRSTY;
    }

    private NotafiscalRSTY buildNotafiscalForClient(final NotafiscalSTY formSTY) {
        // TODO Auto-generated method stub
        return null;
    }



}// class
