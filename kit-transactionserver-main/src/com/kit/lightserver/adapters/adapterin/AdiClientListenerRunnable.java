package com.kit.lightserver.adapters.adapterin;

import java.io.DataInputStream;

import kit.primitives.base.Primitive;
import kit.primitives.factory.PrimitiveStreamFactory;

import org.dajo.configuration.ConfigAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fap.thread.NamedRunnable;
import com.kit.lightserver.adapters.logger.AdaptersLogger;
import com.kit.lightserver.domain.types.ConnectionInfoVO;
import com.kit.lightserver.loggers.connectionlogger.ConnectionsLogger;
import com.kit.lightserver.network.SocketWrapper;
import com.kit.lightserver.statemachine.KITStateMachineRunnable.EventQueue;
import com.kit.lightserver.statemachine.events.AdapterInDataInputClosedSME;
import com.kit.lightserver.statemachine.events.AdapterInErrorTimeOutSME;
import com.kit.lightserver.statemachine.events.AdapterInInterrupedSME;
import com.kit.lightserver.statemachine.events.ServerErrorConvertingPrimitiveSME;
import com.kit.lightserver.statemachine.states.KitEventSME;

public final class AdiClientListenerRunnable implements NamedRunnable {

    static private final Logger LOGGER = LoggerFactory.getLogger(AdiClientListenerRunnable.class);

    static private final int ADAPTER_IN_TIMEOUT_IN_MILLIS = 300000; // 30s in tests

    private final SocketWrapper socket;

    private final ConnectionInfoVO connectionInfo;

    private final EventQueue eventQueue;

    public AdiClientListenerRunnable(final SocketWrapper givenSocket, final ConfigAccessor config, final ConnectionInfoVO connectionInfo, final EventQueue eventQueue) {

        this.socket = givenSocket;
        this.connectionInfo = connectionInfo;
        this.eventQueue = eventQueue;

    }// constructor

    @Override
    public String getThreadNamePrefix() {
        return "T2";
    }

    @Override
    public void run() {

        LOGGER.info("Thread Started");

        try {
            runInternal();
        }
        catch (Throwable t) {
            LOGGER.error("Unexpected error.", t);
        }

        if( socket.isClosed() == true ) {
            ConnectionsLogger.logConnectionClosed(connectionInfo);
        }
        else {
            LOGGER.error("Unexpected! Socket not closed!");
        }

        LOGGER.info("Thread Finished");

    }

    private void runInternal() {

        LOGGER.info("ADI started.");

        try {

            final DataInputStream dataInputStream = socket.getDataInputStream();
            final KitServerDataInput socketDataInput = new KitServerDataInput(dataInputStream);

            boolean dataInputTimeOut = false;

            long totalAvailable = 0;
            long lastEventTime = System.currentTimeMillis();
            while ( socket.dataOutputCanBeClosed()==false && dataInputTimeOut == false ) {

                int availableBytes = dataInputStream.available();
                if (availableBytes > 0) {

                    final Primitive clientPrimitive = PrimitiveStreamFactory.readPrimitive(socketDataInput);
                    lastEventTime = System.currentTimeMillis();

                    totalAvailable += availableBytes;

                    AdaptersLogger.logReceived(clientPrimitive);

                    final ReceivedPrimitiveConverterResult<KitEventSME> converterResult = ReceivedPrimitiveConverter.convert(clientPrimitive);

                    final KitEventSME eventSME;
                    if (converterResult.isSuccess()) {
                        eventSME = converterResult.getEvent();
                    }
                    else {
                        LOGGER.error("Unable to convert primitive, invalid will be sent to state machine. clientPrimitive=" + clientPrimitive);
                        eventSME = new ServerErrorConvertingPrimitiveSME();
                    }

                    eventQueue.enqueueReceived(eventSME);

                }
                else {
                    SimpleSleeper.sleep(100);
                }

                /*
                 * Checking for timeout
                 */
                final long waitingTime = System.currentTimeMillis() - lastEventTime;
                if (waitingTime >= ADAPTER_IN_TIMEOUT_IN_MILLIS) {
                    dataInputTimeOut = true;
                    LOGGER.error("Time out. waitingTime=" + waitingTime);
                    final AdapterInErrorTimeOutSME errorSTY = new AdapterInErrorTimeOutSME(waitingTime);
                    eventQueue.enqueueReceived(errorSTY);
                }// if

            }// while

            LOGGER.info("dataInputStream.totalAvailable="+totalAvailable);
            LOGGER.info("dataInputStream.getTotalBytes="+socketDataInput.getTotalBytes());

        }
        catch (final Exception e) {
            LOGGER.info("Unexpected Error.", e);
            final AdapterInInterrupedSME errorSTY = new AdapterInInterrupedSME(e);
            eventQueue.enqueueReceived(errorSTY);
        }

        LOGGER.info("Not receiving data anymore.");

        final AdapterInDataInputClosedSME eventSME = new AdapterInDataInputClosedSME();
        eventQueue.enqueueReceived(eventSME);

        socket.closeDataInputStream();

        LOGGER.info("runInternal() - finished");

    }

}// class
