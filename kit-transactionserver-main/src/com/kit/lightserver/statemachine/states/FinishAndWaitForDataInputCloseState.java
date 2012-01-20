package com.kit.lightserver.statemachine.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfap.framework.statemachine.ProcessingResult;
import com.jfap.framework.statemachine.ResultStateTransition;
import com.jfap.framework.statemachine.ResultWaitEvent;
import com.jfap.framework.statemachine.StateSME;
import com.kit.lightserver.statemachine.ConversationFinishedStatusCTX;
import com.kit.lightserver.statemachine.KitGeneralCTX;
import com.kit.lightserver.statemachine.events.AdapterInDataInputClosedSME;
import com.kit.lightserver.types.response.CloseDataOutputCommandRSTY;

public final class FinishAndWaitForDataInputCloseState implements StateSME<KitEventSME> {

    static private final Logger LOGGER = LoggerFactory.getLogger(FinishAndWaitForDataInputCloseState.class);

    static public StateSME<KitEventSME> getInstance(final KitGeneralCTX context, final ConversationFinishedStatusCTX conversationFinishedStatus) {
        final FinishAndWaitForDataInputCloseState state = new FinishAndWaitForDataInputCloseState(context, conversationFinishedStatus);
        return state;
    }

    private final KitGeneralCTX context;

    private final ConversationFinishedStatusCTX conversationFinishedStatus;

    private FinishAndWaitForDataInputCloseState(final KitGeneralCTX context, final ConversationFinishedStatusCTX conversationFinishedStatus) {
        this.context = context;
        this.conversationFinishedStatus = conversationFinishedStatus;
    }// constructor

    @Override
    public ProcessingResult<KitEventSME> transitionOccurred() {
        final CloseDataOutputCommandRSTY closeDataOutput = new CloseDataOutputCommandRSTY();
        context.getClientAdapterOut().sendBack(closeDataOutput);
        return new ResultWaitEvent<KitEventSME>();
    }

    @Override
    public ProcessingResult<KitEventSME> processEvent(final KitEventSME event) {
        if ( (event instanceof AdapterInDataInputClosedSME) == false) {
            LOGGER.error("Unexpected event, discarding. event="+event);
        }
        final StateSME<KitEventSME> endedState = new MachineEndedState(context, conversationFinishedStatus);
        return new ResultStateTransition<KitEventSME>(endedState);
    }

}// class
