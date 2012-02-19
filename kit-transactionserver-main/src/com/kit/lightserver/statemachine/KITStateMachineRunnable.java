package com.kit.lightserver.statemachine;

import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfap.framework.configuration.ConfigAccessor;
import com.jfap.framework.statemachine.StateMachine;
import com.jfap.framework.statemachine.StateSME;
import com.kit.lightserver.adapters.adapterout.ClientAdapterOut;
import com.kit.lightserver.domain.types.ConnectionInfo;
import com.kit.lightserver.statemachine.states.InitialState;
import com.kit.lightserver.statemachine.states.KitEventSME;
import com.kit.lightserver.types.response.ClientResponseRSTY;

public final class KITStateMachineRunnable implements Runnable {

    static private final Logger LOGGER = LoggerFactory.getLogger(KITStateMachineRunnable.class);

    private final List<KitEventSME> requestsReceivedQueue = Collections.synchronizedList(new LinkedList<KitEventSME>());

    private final List<ClientResponseRSTY> responseToSendQueue = Collections.synchronizedList(new LinkedList<ClientResponseRSTY>());

    private final StateMachine<KitEventSME> kitStateState;

    private final StateMachineMainContext stateMachineContext;

    private boolean canEnqueue = false; // In the runnable I can not access thread.isAlive()

    private final ClientAdapterOut clientAdapterOut;

    public KITStateMachineRunnable(final Socket socket, final ConfigAccessor configAccessor, final ConnectionInfo connectionId) {

        this.clientAdapterOut = new ClientAdapterOut(socket);
        this.stateMachineContext = new StateMachineMainContext(clientAdapterOut, configAccessor, connectionId);
        this.kitStateState = new StateMachine<KitEventSME>();

    }// constructor

    private synchronized void setCanEnqueue(final boolean canEnqueue) { //TODO: Remove all synchronized and use locks
        this.canEnqueue = canEnqueue;
    }

    public synchronized boolean enqueueReceived(final KitEventSME event) {
        if(  canEnqueue == false ) {
            LOGGER.warn("Could not enqueue " + event);
            return false;
        }
        requestsReceivedQueue.add(event);
        return true;
    }

    public synchronized boolean enqueueToSend(final ClientResponseRSTY clientResponseRSTY) {
        if(  canEnqueue == false ) {
            LOGGER.warn("Could not enqueue " + clientResponseRSTY);
            return false;
        }
        responseToSendQueue.add(clientResponseRSTY);
        return true;
    }

    @Override
    public void run() { // be careful it can never be synchronized can cause deadlocks


        final StateSME<KitEventSME> initialState = new InitialState(stateMachineContext);
        kitStateState.start(initialState);

        setCanEnqueue(true);

        LOGGER.info("Thread Started");

        boolean isMachineProcessingExternalEvents = true;
        while(isMachineProcessingExternalEvents || responseToSendQueue.size() > 0 || requestsReceivedQueue.size() > 0) {

            boolean shouldThreadSleep = true;

            if(responseToSendQueue.size() > 0) {

                while( responseToSendQueue.size() > 0 ) {

                    final ClientResponseRSTY clientResponse = responseToSendQueue.remove(0);
                    if( clientAdapterOut.isValidToSend() ) {
                        clientAdapterOut.sendBack(clientResponse);
                    }
                    else {
                        LOGGER.error("Adapter out can not send. Primitive pending will be discarted. clientResponse="+clientResponse);
                    }

                }// while

                shouldThreadSleep = false;

            }// if

            if(requestsReceivedQueue.size() > 0) {
                final KitEventSME primitive = requestsReceivedQueue.remove(0);
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

        setCanEnqueue(false);

        if(requestsReceivedQueue.size() > 0 ) {
            LOGGER.error("The state machine thread has finished with unprocessed primitives. requestsReceivedQueue=" + requestsReceivedQueue);
        }

        LOGGER.info("Thread Finished");

    }


}// class
