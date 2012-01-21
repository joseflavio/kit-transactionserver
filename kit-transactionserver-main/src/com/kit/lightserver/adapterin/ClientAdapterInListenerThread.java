package com.kit.lightserver.adapterin;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

import kit.primitives.base.Primitive;
import kit.primitives.factory.PrimitiveStreamFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kit.lightserver.domain.types.ConnectionId;
import com.kit.lightserver.statemachine.KITStateMachineRunnable;
import com.kit.lightserver.statemachine.events.AdapterInDataInputClosedSME;
import com.kit.lightserver.statemachine.events.AdapterInErrorTimeOutSME;
import com.kit.lightserver.statemachine.events.AdapterInInterrupedSME;
import com.kit.lightserver.statemachine.events.ServerErrorConvertingPrimitiveSME;
import com.kit.lightserver.statemachine.states.KitEventSME;


public final class ClientAdapterInListenerThread implements Runnable {

    static private final Logger LOGGER = LoggerFactory.getLogger(ClientAdapterInListenerThread.class);

    static private final int ADAPTER_IN_TIMEOUT_IN_MILLIS = 30000; //180000;// 180000=180s in production and 7000=7s in tests

    private final Socket socket;

    private final KITStateMachineRunnable kitStateMachineRunnable;

    private final Thread kitStateMachineThread;

    public ClientAdapterInListenerThread(final Socket givenSocket, final ConnectionId connectionId) {
        this.socket = givenSocket;
        this.kitStateMachineRunnable = new KITStateMachineRunnable(givenSocket, connectionId);
        this.kitStateMachineThread = new Thread(kitStateMachineRunnable);
    }// constructor

    @Override
    public void run() {

        LOGGER.info("Thread Started");

        kitStateMachineThread.start();

        final Sleeper sleeper = new Sleeper(new SleeperAwakeEvent() {
            @Override
            public boolean shouldWakeUp() {
                return kitStateMachineThread.isAlive();
            }
        });

        final boolean eventOccurred = sleeper.waitFor(2000);

        if (eventOccurred == false) {
            return;
        }

        try {

            final DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            boolean dataInputAlive = true;

            long lastEventTime = System.currentTimeMillis();
            while ((socket.isClosed() == false) && dataInputAlive && kitStateMachineThread.isAlive()) {

                if (dataInputStream.available() > 0) {

                    final Primitive clientPrimitive = PrimitiveStreamFactory.readPrimitive(dataInputStream);
                    lastEventTime = System.currentTimeMillis();
                    LOGGER.info("Received primitive. clientPrimitive=" + clientPrimitive);

                    final ReceivedPrimitiveConverterResult<KitEventSME> converterResult = ReceivedPrimitiveConverter.convert(clientPrimitive);

                    final KitEventSME eventSME;
                    if( converterResult.isSuccess() ) {
                        eventSME = converterResult.getEvent();
                    }
                    else {
                        LOGGER.error("Unable to convert primitive, invalid will be sent to state machine. clientPrimitive=" + clientPrimitive);
                        eventSME = new ServerErrorConvertingPrimitiveSME();
                    }

                    kitStateMachineRunnable.enqueueReceived(eventSME);

                }
                else {
                    SimpleSleeper.sleep(50);
                }// if-else

                /*
                 * Checking for timeout
                 */
                final long waitingTime = System.currentTimeMillis() - lastEventTime;

                if (waitingTime >= ADAPTER_IN_TIMEOUT_IN_MILLIS) {
                    LOGGER.error("Time out. waitingTime=" + waitingTime);
                    dataInputAlive = false;
                    final AdapterInErrorTimeOutSME errorSTY = new AdapterInErrorTimeOutSME(waitingTime);
                    kitStateMachineRunnable.enqueueReceived(errorSTY);
                }// if

            }// while

        }
        catch (final EOFException e) {
            LOGGER.info("Unexpected EOF.", e);
            final AdapterInInterrupedSME errorSTY = new AdapterInInterrupedSME(e);
            kitStateMachineRunnable.enqueueReceived(errorSTY);
        }
        catch (final IOException e) {
            LOGGER.info("Interrupted", e);
            final AdapterInInterrupedSME errorSTY = new AdapterInInterrupedSME(e);
            kitStateMachineRunnable.enqueueReceived(errorSTY);
        }// try-catch

        final AdapterInDataInputClosedSME eventSME = new AdapterInDataInputClosedSME();
        kitStateMachineRunnable.enqueueReceived(eventSME);

        LOGGER.info("Thread Finished");

    }

}// class
