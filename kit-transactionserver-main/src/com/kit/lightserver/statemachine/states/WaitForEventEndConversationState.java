package com.kit.lightserver.statemachine.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fap.framework.statemachine.ProcessingResult;
import com.fap.framework.statemachine.ResultStateTransition;
import com.fap.framework.statemachine.ResultWaitEvent;
import com.fap.framework.statemachine.StateSME;

import com.kit.lightserver.statemachine.StateMachineMainContext;
import com.kit.lightserver.statemachine.events.ChannelNotificationSME;
import com.kit.lightserver.statemachine.types.ConversationFinishedStatusCTX;
import com.kit.lightserver.types.response.ChannelNotificationEndConversationRSTY;

public final class WaitForEventEndConversationState implements StateSME<KitEventSME> {

    static private final Logger LOGGER = LoggerFactory.getLogger(WaitForEventEndConversationState.class);

    static public StateSME<KitEventSME> getInstance(final StateMachineMainContext context) {
        WaitForEventEndConversationState state = new WaitForEventEndConversationState(context);
        return state;
    }

    private final StateMachineMainContext context;

    private WaitForEventEndConversationState(final StateMachineMainContext context) {
        this.context = context;
    }// constructor

    @Override
    public ProcessingResult<KitEventSME> transitionOccurred() {

        ChannelNotificationEndConversationRSTY clientEndConversation = new ChannelNotificationEndConversationRSTY();
        context.getClientAdapterOut().sendBack(clientEndConversation);

        return new ResultWaitEvent<>();
    }

    @Override
    public ProcessingResult<KitEventSME> processEvent(final KitEventSME event) {

        final ProcessingResult<KitEventSME> result;

        if( (event instanceof ChannelNotificationSME) == false ) {
            LOGGER.error("Unexpected Event. event="+event);
            StateSME<KitEventSME> errorState = UnrecoverableErrorState.getInstance(context, ConversationFinishedStatusCTX.FINISHED_ERROR_UNEXPECTED_EVENT);
            result = new ResultStateTransition<>(errorState);
        }
        else {

            ChannelNotificationSME channelNotificationEvent = (ChannelNotificationSME)event;
            if( channelNotificationEvent.getType() == ChannelNotificationSME.Type.END_CHANNEL ) {
                StateSME<KitEventSME> finishedState = FinishAndWaitForDataInputCloseState.getInstance(context, ConversationFinishedStatusCTX.FINISHED_WITH_SUCCESS);
                result = new ResultStateTransition<>(finishedState);
            }
            else {
                LOGGER.warn("Problem in the protocol (Maybe an old mobile client). event="+event);
                result = new ResultWaitEvent<>();
            }
        }

        return result;

    }

}// class
