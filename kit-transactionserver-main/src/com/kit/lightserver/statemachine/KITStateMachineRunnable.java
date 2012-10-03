package com.kit.lightserver.statemachine;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.dajo.configuration.ConfigAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fap.framework.statemachine.StateMachine;
import com.fap.framework.statemachine.StateSME;
import com.fap.thread.NamedRunnable;
import com.kit.lightserver.adapters.adapterout.ClientAdapterOut;
import com.kit.lightserver.domain.types.ConnectionInfoVO;
import com.kit.lightserver.network.SocketWrapper;
import com.kit.lightserver.statemachine.states.InitialState;
import com.kit.lightserver.statemachine.states.KitEventSME;

public final class KITStateMachineRunnable implements NamedRunnable {

    static private final Logger LOGGER = LoggerFactory.getLogger(KITStateMachineRunnable.class);

    private final StateMachine<KitEventSME> kitStateState;

    private final StateMachineMainContext stateMachineContext;

    private final EventQueue eventQueue = new EventQueue();

    private final ClientAdapterOut clientAdapterOut;

    public KITStateMachineRunnable(final SocketWrapper socket, final ConfigAccessor configAccessor, final ConnectionInfoVO connectionInfo) {

        this.clientAdapterOut = new ClientAdapterOut(socket, eventQueue);
        this.stateMachineContext = new StateMachineMainContext(clientAdapterOut, configAccessor, connectionInfo);
        this.kitStateState = new StateMachine<KitEventSME>();

    }// constructor

    @Override
    public String getThreadNamePrefix() {
        return "T1";
    }

    public EventQueue getEventQueue() {
        return eventQueue;
    }

    @Override
    public void run() { // be careful it can never be synchronized can cause deadlocks


        final StateSME<KitEventSME> initialState = new InitialState(stateMachineContext);
        kitStateState.start(initialState);

        eventQueue.setCanEnqueue(true);

        LOGGER.info("Thread Started");

        boolean isMachineProcessingExternalEvents = true;
        while(isMachineProcessingExternalEvents || eventQueue.hasEvents() == true ) {

            boolean shouldThreadSleep = true;

            if( eventQueue.hasEvents() == true ) {
                final KitEventSME primitive = eventQueue.dequeueFirst();
                isMachineProcessingExternalEvents = kitStateState.processExternalEvent(primitive);
                shouldThreadSleep = false;
            }// if

            if( shouldThreadSleep ) {
                try {
                    Thread.sleep(250);
                } catch (final InterruptedException e) {
                    LOGGER.error("Unexpected interruption", e);
                }
            }// if

        }// while

        eventQueue.setCanEnqueue(false);

        if( eventQueue.hasEvents() == true ) {
            LOGGER.error("The state machine thread has finished with unprocessed primitives. eventQueue=" + eventQueue);
        }

        LOGGER.info("Thread Finished");

    }

    static public final class EventQueue {

        private boolean canEnqueue = false;

        private final List<KitEventSME> requestsReceivedQueue = Collections.synchronizedList(new LinkedList<KitEventSME>());

        protected synchronized void setCanEnqueue(final boolean canEnqueue) { //TODO: Remove all synchronized and use locks
            this.canEnqueue = canEnqueue;
        }

        protected synchronized KitEventSME dequeueFirst() {
            return  requestsReceivedQueue.remove(0);
        }

        protected synchronized boolean hasEvents() {
            if( requestsReceivedQueue.size() > 0 ) {
                return true;
            }
            return false;
        }

        public synchronized boolean enqueueReceived(final KitEventSME event) {
            if(  canEnqueue == false ) {
                LOGGER.warn("Could not enqueue " + event);
                return false;
            }
            requestsReceivedQueue.add(event);
            return true;
        }

    }

}// class
