package com.kit.lightserver.statemachine.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fap.framework.statemachine.ProcessingResult;
import com.fap.framework.statemachine.ResultStateTransition;
import com.fap.framework.statemachine.ResultWaitEvent;
import com.fap.framework.statemachine.StateSME;

import com.kit.lightserver.services.be.forms.FormServices;
import com.kit.lightserver.statemachine.StateMachineMainContext;
import com.kit.lightserver.statemachine.events.FormContentConhecimentoReadSME;
import com.kit.lightserver.statemachine.events.FormContentEditedSME;
import com.kit.lightserver.statemachine.events.FormOperationUpdateFormsCompleteEventSME;
import com.kit.lightserver.statemachine.types.ConversationFinishedStatusCTX;
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
        LOGGER.info("Requesting updated forms");
        FormOperationUpdatedFormsRequestRSTY formOperationUpdatedRowsGetRSTY = new FormOperationUpdatedFormsRequestRSTY();
        context.getClientAdapterOut().sendBack(formOperationUpdatedRowsGetRSTY);
        return new ResultWaitEvent<KitEventSME>();
    }

    @Override
    public ProcessingResult<KitEventSME> processEvent(final KitEventSME event) {

        LOGGER.info("event=" + event);

        final String clientUserId = context.getClientInfo().getKtClientUserId();

        final ProcessingResult<KitEventSME> result;
        if (event instanceof FormContentConhecimentoReadSME) {
            FormContentConhecimentoReadSME formReadEvent = (FormContentConhecimentoReadSME)event;
            boolean serviceSuccess = formServices.saveFormFirstRead(clientUserId, formReadEvent.getClientRowId(), formReadEvent.getFirstReadDate());
            if (serviceSuccess == false) {
                final StateSME<KitEventSME> errorState = UnrecoverableErrorState.getInstance(context, ConversationFinishedStatusCTX.FINISHED_GENERAL_ERROR);
                result = new ResultStateTransition<KitEventSME>(errorState);
            }
            else {
                result = new ResultWaitEvent<KitEventSME>();
            }
        }
        else if( event instanceof FormContentEditedSME ) {
            FormContentEditedSME formEditedEvent = (FormContentEditedSME) event;
            boolean serviceSuccess = formServices.saveFormEdited(
                    clientUserId, formEditedEvent.getConhecimentoId(), formEditedEvent.getStatusEntregaEnumSTY(), formEditedEvent.getDataEntrega() );
            if (serviceSuccess == false) {
                final StateSME<KitEventSME> errorState = UnrecoverableErrorState.getInstance(context, ConversationFinishedStatusCTX.FINISHED_GENERAL_ERROR);
                result = new ResultStateTransition<KitEventSME>(errorState);
            }
            else {
                result = new ResultWaitEvent<KitEventSME>();
            }
        }
        else if ( event instanceof FormOperationUpdateFormsCompleteEventSME ) {
            FormOperationUpdatedFormsClearFlagsRSTY formOperationClearFlags = new FormOperationUpdatedFormsClearFlagsRSTY();
            context.getClientAdapterOut().sendBack(formOperationClearFlags);
            StateSME<KitEventSME> newState = GpsActivityState.getInstance(context);
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
