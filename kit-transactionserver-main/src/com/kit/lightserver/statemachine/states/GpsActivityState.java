package com.kit.lightserver.statemachine.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fap.framework.statemachine.ProcessingResult;
import com.fap.framework.statemachine.ResultStateTransition;
import com.fap.framework.statemachine.ResultWaitEvent;
import com.fap.framework.statemachine.StateSME;

import com.kit.lightserver.statemachine.StateMachineMainContext;
import com.kit.lightserver.statemachine.events.ActivityGpsSME;
import com.kit.lightserver.statemachine.types.ConversationFinishedStatusCTX;
import com.kit.lightserver.types.response.SimpleRSTY;

public class GpsActivityState extends BaseState implements StateSME<KitEventSME> {

    static private final Logger LOGGER = LoggerFactory.getLogger(GpsActivityState.class);

    static public StateSME<KitEventSME> getInstance(final StateMachineMainContext context) {
        return new GpsActivityState(context);
    }

    private GpsActivityState(final StateMachineMainContext context) {
        super(context);
    }

    @Override
    public ProcessingResult<KitEventSME> transitionOccurred() {
        boolean isGpsEnabled = super.context.getClientInfo().isGpsEnabled();
        if( isGpsEnabled ) {
            SimpleRSTY requestGps = new SimpleRSTY(SimpleRSTY.Type.ACTIVITY_GPS_REQUEST_LOG_LINE);
            context.getClientAdapterOut().sendBack(requestGps);
            ResultWaitEvent<KitEventSME> result = new ResultWaitEvent<KitEventSME>();
            return result;
        }
        else {
            StateSME<KitEventSME> newState = WaitForEventEndConversationState.getInstance(context);
            ResultStateTransition<KitEventSME> result = new ResultStateTransition<KitEventSME>(newState);
            return result;
        }
    }

    @Override
    public ProcessingResult<KitEventSME> processEvent(final KitEventSME event) {

        if( event instanceof ActivityGpsSME == false ) {
            LOGGER.error("Unexpected Event. event={}", event);
            return new ResultStateTransition<>(UnrecoverableErrorState.getInstance(context, ConversationFinishedStatusCTX.FINISHED_ERROR_UNEXPECTED_EVENT));
        }

        StateSME<KitEventSME> newState = WaitForEventEndConversationState.getInstance(context);
        ResultStateTransition<KitEventSME> result = new ResultStateTransition<KitEventSME>(newState);
        return result;
    }

}// class
