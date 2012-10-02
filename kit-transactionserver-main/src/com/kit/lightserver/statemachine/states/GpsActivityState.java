package com.kit.lightserver.statemachine.states;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fap.framework.statemachine.ProcessingResult;
import com.fap.framework.statemachine.ResultStateTransition;
import com.fap.framework.statemachine.ResultWaitEvent;
import com.fap.framework.statemachine.StateSME;

import com.kit.lightserver.domain.types.CoordenadaGpsSTY;
import com.kit.lightserver.services.be.gps.GpsService;
import com.kit.lightserver.statemachine.StateMachineMainContext;
import com.kit.lightserver.statemachine.events.ActivityGpsSME;
import com.kit.lightserver.statemachine.types.ConversationFinishedStatusCTX;
import com.kit.lightserver.types.response.SimpleRSTY;

public class GpsActivityState extends BaseState implements StateSME<KitEventSME> {

    static private final Logger LOGGER = LoggerFactory.getLogger(GpsActivityState.class);

    static public StateSME<KitEventSME> getInstance(final StateMachineMainContext context) {
        return new GpsActivityState(context);
    }

    private final List<CoordenadaGpsSTY> coordenadasReceived = new LinkedList<>();

    private GpsActivityState(final StateMachineMainContext context) {
        super(context);
    }

    @Override
    public ProcessingResult<KitEventSME> transitionOccurred() {
        boolean isGpsEnabled = super.context.getClientInfo().isGpsEnabled();
        if( isGpsEnabled ) {
            SimpleRSTY requestGps = new SimpleRSTY(SimpleRSTY.Type.ACTIVITY_GPS_REQ_LOG_LINE_LAST_ENTRIES);
            context.getClientAdapterOut().sendBack(requestGps);
            return new ResultWaitEvent<>();
        }
        else {
            StateSME<KitEventSME> newState = WaitForEventEndConversationState.getInstance(context);
            ResultStateTransition<KitEventSME> result = new ResultStateTransition<>(newState);
            return result;
        }
    }

    @Override
    public ProcessingResult<KitEventSME> processEvent(final KitEventSME event) {

        if( event instanceof ActivityGpsSME == false ) {
            LOGGER.error("Unexpected Event. event={}", event);
            return new ResultStateTransition<>(UnrecoverableErrorState.getInstance(context, ConversationFinishedStatusCTX.FINISHED_ERROR_UNEXPECTED_EVENT));
        }

        ActivityGpsSME activityGps = (ActivityGpsSME)event;
        coordenadasReceived.addAll(activityGps.getCoordenadas());

        if( activityGps.isMoreActivitiesAvailable() == true ) {
            SimpleRSTY delete = new SimpleRSTY(SimpleRSTY.Type.ACTIVITY_GPS_CMD_DELETE_LAST_ENTRIES);
            SimpleRSTY requestMore = new SimpleRSTY(SimpleRSTY.Type.ACTIVITY_GPS_REQ_LOG_LINE_LAST_ENTRIES);
            context.getClientAdapterOut().sendBack(delete, requestMore);
            return new ResultWaitEvent<>();
        }

        GpsService.getInstance(context.getConfigAccessor()).logGpsActivities(
                context.getConnectionInfo(), context.getClientInfo().getKtClientUserId(), context.getClientInfo().getClientInstallId(), coordenadasReceived);

        StateSME<KitEventSME> newState = WaitForEventEndConversationState.getInstance(context);
        return ResultStateTransition.getInstance(newState);

    }

}// class
