package com.fap.framework.statemachine;


public final class ResultStateTransition<T> extends ProcessingResult<T> {

    private final StateSME<T> newState;

    public ResultStateTransition(final StateSME<T> newState) {
        this.newState = newState;
    }

    public StateSME<T> getNewState() {
        return newState;
    }

}// class
