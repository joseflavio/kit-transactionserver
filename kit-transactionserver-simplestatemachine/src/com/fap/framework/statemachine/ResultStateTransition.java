package com.fap.framework.statemachine;


public final class ResultStateTransition<T> extends ProcessingResult<T> {

    private final StateSME<T> newState;

    public ResultStateTransition(final StateSME<T> newState) {
        this.newState = newState;
    }

    static public <T> ResultStateTransition<T> getInstance(final StateSME<T> newState) {
        return new ResultStateTransition<>(newState);
    }

    public StateSME<T> getNewState() {
        return newState;
    }

}// class
