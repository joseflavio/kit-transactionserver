package com.kit.lightserver.statemachine.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fap.framework.statemachine.ProcessingResult;
import com.fap.framework.statemachine.ResultStateTransition;
import com.fap.framework.statemachine.ResultWaitEvent;
import com.fap.framework.statemachine.StateSME;

import com.kit.lightserver.adapters.adapterout.AdoPrimitiveListEnvelope;
import com.kit.lightserver.statemachine.StateMachineMainContext;
import com.kit.lightserver.statemachine.types.ConversationFinishedStatusCTX;
import com.kit.lightserver.types.response.ChannelNotificationServerErrorRSTY;

public final class UnrecoverableErrorState implements StateSME<KitEventSME> {

    static private final Logger LOGGER = LoggerFactory.getLogger(ClientAuthenticationSuccessfulState.class);

    static public StateSME<KitEventSME> getInstance(final StateMachineMainContext context, final ConversationFinishedStatusCTX conversationFinishedStatus) {
        final UnrecoverableErrorState state = new UnrecoverableErrorState(context, conversationFinishedStatus);
        return state;
    }

    private final StateMachineMainContext context;
    private final ConversationFinishedStatusCTX conversationFinishedStatus;

    private UnrecoverableErrorState(final StateMachineMainContext context, final ConversationFinishedStatusCTX conversationFinishedStatus) {
        this.context = context;
        this.conversationFinishedStatus = conversationFinishedStatus;
    }// constructor

    @Override
    public ProcessingResult<KitEventSME> transitionOccurred() {
        ChannelNotificationServerErrorRSTY error = new ChannelNotificationServerErrorRSTY();
        AdoPrimitiveListEnvelope primitivesEnvelope = new AdoPrimitiveListEnvelope(error);
        context.getClientAdapterOut().sendBack(primitivesEnvelope);
        StateSME<KitEventSME> newState = FinishAndWaitForDataInputCloseState.getInstance(context, conversationFinishedStatus);
        ResultStateTransition<KitEventSME> transition = new ResultStateTransition<KitEventSME>(newState);
        return transition;
    }

    @Override
    public ProcessingResult<KitEventSME> processEvent(final KitEventSME event) {
        LOGGER.error("Unexpected event. event=" + event);
        return new ResultWaitEvent<KitEventSME>();
    }

}// class
