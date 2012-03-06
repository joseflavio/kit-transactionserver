package com.kit.lightserver.statemachine.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfap.framework.statemachine.ProcessingResult;
import com.jfap.framework.statemachine.ResultStateTransition;
import com.jfap.framework.statemachine.ResultWaitEvent;
import com.jfap.framework.statemachine.StateSME;
import com.kit.lightserver.adapters.adapterout.AdoPrimitiveListEnvelope;
import com.kit.lightserver.services.be.forms.FormServices;
import com.kit.lightserver.statemachine.StateMachineMainContext;
import com.kit.lightserver.statemachine.events.FormContentConhecimentoReadSME;
import com.kit.lightserver.statemachine.events.FormOperationUpdateFormsCompleteEventSME;
import com.kit.lightserver.statemachine.types.ConversationFinishedStatusCTX;
import com.kit.lightserver.types.response.ChannelNotificationEndConversationRSTY;
import com.kit.lightserver.types.response.FormOperationUpdatedFormsClearFlagsRSTY;
import com.kit.lightserver.types.response.FormOperationUpdatedFormsRequestRSTY;

final class RetrieveUpdatedFormsState extends BaseState implements StateSME<KitEventSME> {

    static private final Logger LOGGER = LoggerFactory.getLogger(RetrieveUpdatedFormsState.class);

    private final FormServices formServices = FormServices.getInstance(context.getConfigAccessor());

    protected RetrieveUpdatedFormsState(final StateMachineMainContext context) {
        super(context);
    }

    @Override
    public ProcessingResult<KitEventSME> transitionOccurred() {
        FormOperationUpdatedFormsRequestRSTY formOperationUpdatedRowsGetRSTY = new FormOperationUpdatedFormsRequestRSTY();
        AdoPrimitiveListEnvelope primitivesEnvelope = new AdoPrimitiveListEnvelope(formOperationUpdatedRowsGetRSTY);
        context.getClientAdapterOut().sendBack(primitivesEnvelope);
        return new ResultWaitEvent<KitEventSME>();
    }

    @Override
    public ProcessingResult<KitEventSME> processEvent(final KitEventSME event) {

        final ProcessingResult<KitEventSME> result;

        if (event instanceof FormContentConhecimentoReadSME) {

            // FormContentConhecimentoReadSME [conhecimentoId=ConhecimentoIdSTY [ktRowId=1094966], firstReadDate=Tue Mar 06 22:29:56 CET 2012]
            FormContentConhecimentoReadSME formReadEvent = (FormContentConhecimentoReadSME)event;
            LOGGER.info("Testing event. event=" + event);

            final String ktClientId = context.getClientInfo().getKtClientId();
            final boolean serviceSuccess = formServices.flagFormsAsRead(ktClientId, formReadEvent.getConhecimentoId(), formReadEvent.getFirstReadDate());

            if (serviceSuccess == false) {
                final StateSME<KitEventSME> errorState = UnrecoverableErrorState.getInstance(context, ConversationFinishedStatusCTX.FINISHED_GENERAL_ERROR);
                result = new ResultStateTransition<KitEventSME>(errorState);
            }
            else {
                result = new ResultWaitEvent<KitEventSME>();
            }

        }
        else if (event instanceof FormOperationUpdateFormsCompleteEventSME) {
            FormOperationUpdatedFormsClearFlagsRSTY formOperationClearFlags = new FormOperationUpdatedFormsClearFlagsRSTY();
            ChannelNotificationEndConversationRSTY channelNotificationEndConversationRSTY = new ChannelNotificationEndConversationRSTY();
            AdoPrimitiveListEnvelope primitivesEnvelope = new AdoPrimitiveListEnvelope(formOperationClearFlags, channelNotificationEndConversationRSTY);
            context.getClientAdapterOut().sendBack(primitivesEnvelope);
            StateSME<KitEventSME> newState = WaitForEventEndConversationState.getInstance(context);
            result = new ResultStateTransition<KitEventSME>(newState);
        }
        else {
            LOGGER.error("Invalid event. event=" + event);
            StateSME<KitEventSME> errorState = UnrecoverableErrorState.getInstance(context, ConversationFinishedStatusCTX.FINISHED_ERROR_UNEXPECTED_EVENT);
            result = new ResultStateTransition<KitEventSME>(errorState);
        }

        return result;

    }

}// class
