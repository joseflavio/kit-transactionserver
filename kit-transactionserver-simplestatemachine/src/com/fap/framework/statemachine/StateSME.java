package com.fap.framework.statemachine;


public interface StateSME<T> {

    ProcessingResult<T> transitionOccurred();

    ProcessingResult<T> processEvent(T event);

}// interface
