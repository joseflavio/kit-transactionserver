package com.fap.framework.statemachine;

import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class StateMachine<T> {

    static private final Logger LOGGER = LoggerFactory.getLogger(StateMachine.class);

    private StateSME<T> currentState = null;

    public StateMachine() {
    }// constructor

    public boolean processExternalEvent(final T event) {
        final String currentStateClassName = currentState.getClass().getCanonicalName();
        LOGGER.info("Processing Event. state=" + currentStateClassName + ", event=" + event);
        final ProcessingResult<T> processEventResult = currentState.processEvent(event);
        final boolean isMachineStillAlive = processResults(processEventResult);
        return isMachineStillAlive;
    }

    public synchronized void start(final StateSME<T> startState) {
        final ResultStateTransition<T> transition = new ResultStateTransition<T>(startState);
        this.processResults(transition);
    }

    private boolean processResults(final ProcessingResult<T> processResult) {

        final ReentrantLock lock = new ReentrantLock();
        lock.lock();
        boolean isMachineStillAlive = false;
        try {

            ProcessingResult<T> currentResult = processResult;
            boolean shouldContinue = false;

            do {

                if (currentResult instanceof ResultStateTransition) {
                    final ResultStateTransition<T> resultTransition = (ResultStateTransition<T>) currentResult;
                    final StateSME<T> newState = resultTransition.getNewState();
                    LOGGER.info("Transition from " + currentState + " to " + newState);
                    currentState = newState;
                    currentResult = currentState.transitionOccurred();
                    shouldContinue = true;
                    isMachineStillAlive = true;
                }
                else if (currentResult instanceof ResultWaitEvent) {
                    shouldContinue = false;
                    isMachineStillAlive = true;
                }
                else if (currentResult instanceof ResultMachineStopped) {
                    shouldContinue = false;
                    isMachineStillAlive = false;
                }
                else {
                    LOGGER.error("Invalid result type. currentResult=" + currentResult);
                    shouldContinue = false;
                    isMachineStillAlive = false;
                }

            } while (shouldContinue);

        }
        finally {
            lock.unlock();
        }

        return isMachineStillAlive;
    }

}// class

