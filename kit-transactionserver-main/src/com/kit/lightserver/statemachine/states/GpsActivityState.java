package com.kit.lightserver.statemachine.states;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dajo.math.IntegerUtils;

import com.fap.framework.statemachine.ProcessingResult;
import com.fap.framework.statemachine.ResultStateTransition;
import com.fap.framework.statemachine.ResultWaitEvent;
import com.fap.framework.statemachine.StateSME;

import com.kit.lightserver.domain.types.CoordenadaGpsSTY;
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

        Collections.sort( coordenadasReceived, new LogicalClockComparator() );

        LOGGER.info("Coordenadas recebidas: ");
        for (int i = 0; i < coordenadasReceived.size(); i++) {
            LOGGER.info(i + " - " + coordenadasReceived.get(i));
        }

        StateSME<KitEventSME> newState = WaitForEventEndConversationState.getInstance(context);
        return ResultStateTransition.getInstance(newState);

    }

    static private final class LogicalClockComparator implements Comparator<CoordenadaGpsSTY>, Serializable {
        static private final long serialVersionUID = 1L;
        @Override
        public int compare(final CoordenadaGpsSTY o1, final CoordenadaGpsSTY o2) {
            return IntegerUtils.safeLongToInt( o1.getLogicalClock() - o2.getLogicalClock() );
        }
    }

}// class
