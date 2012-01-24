package com.kit.lightserver.statemachine.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfap.framework.statemachine.ProcessingResult;
import com.jfap.framework.statemachine.ResultStateTransition;
import com.jfap.framework.statemachine.ResultWaitEvent;
import com.jfap.framework.statemachine.StateSME;
import com.kit.lightserver.statemachine.StateMachineMainContext;
import com.kit.lightserver.statemachine.events.ChannelNotificationEndConversationSME;
import com.kit.lightserver.statemachine.types.ConversationFinishedStatusCTX;

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
        return new ResultWaitEvent<KitEventSME>();
    }

    @Override
    public ProcessingResult<KitEventSME> processEvent(final KitEventSME event) {

        final StateSME<KitEventSME> newState;

        if( (event instanceof ChannelNotificationEndConversationSME) == false) {
            LOGGER.error("Unexpected Event. event="+event);
            newState = UnrecoverableErrorState.getInstance(context, ConversationFinishedStatusCTX.FINISHED_ERROR_UNEXPECTED_EVENT);
        }
        else {
            newState = FinishAndWaitForDataInputCloseState.getInstance(context, ConversationFinishedStatusCTX.FINISHED_WITH_SUCCESS);
        }

        return new ResultStateTransition<KitEventSME>(newState);

    }

}// class
