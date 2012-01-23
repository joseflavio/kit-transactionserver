package com.kit.lightserver.statemachine.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfap.framework.statemachine.ProcessingResult;
import com.jfap.framework.statemachine.ResultStateTransition;
import com.jfap.framework.statemachine.ResultWaitEvent;
import com.jfap.framework.statemachine.StateSME;
import com.kit.lightserver.statemachine.ConversationFinishedStatusCTX;
import com.kit.lightserver.statemachine.KitGeneralCTX;
import com.kit.lightserver.statemachine.events.FormContentConhecimentoReadSME;
import com.kit.lightserver.statemachine.events.FormOperationUpdateFormsCompleteEventSME;
import com.kit.lightserver.types.response.ChannelNotificationEndConversationRSTY;
import com.kit.lightserver.types.response.FormOperationUpdatedFormsClearFlagsRSTY;
import com.kit.lightserver.types.response.FormOperationUpdatedFormsRequestRSTY;

final class RetrieveUpdatedFormsState extends KitTransactionalAbstractState implements StateSME<KitEventSME> {

    static private final Logger LOGGER = LoggerFactory.getLogger(RetrieveUpdatedFormsState.class);

    protected RetrieveUpdatedFormsState(final KitGeneralCTX context) {
        super(context);
    }

    @Override
    public ProcessingResult<KitEventSME> transitionOccurred() {
        final FormOperationUpdatedFormsRequestRSTY formOperationUpdatedRowsGetRSTY = new FormOperationUpdatedFormsRequestRSTY();
        context.getClientAdapterOut().sendBack(formOperationUpdatedRowsGetRSTY);
        return new ResultWaitEvent<KitEventSME>();
    }

    @Override
    public ProcessingResult<KitEventSME> processEvent(final KitEventSME event) {

        final ProcessingResult<KitEventSME> result;

        if (event instanceof FormContentConhecimentoReadSME) {
            LOGGER.error("Unimplemented event. event=" + event);
            result = new ResultWaitEvent<KitEventSME>();
        }
        else if (event instanceof FormOperationUpdateFormsCompleteEventSME) {

            final FormOperationUpdatedFormsClearFlagsRSTY formOperationClearFlags = new FormOperationUpdatedFormsClearFlagsRSTY();
            context.getClientAdapterOut().sendBack(formOperationClearFlags);

            final ChannelNotificationEndConversationRSTY channelNotificationEndConversationRSTY = new ChannelNotificationEndConversationRSTY();
            context.getClientAdapterOut().sendBack(channelNotificationEndConversationRSTY);

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
