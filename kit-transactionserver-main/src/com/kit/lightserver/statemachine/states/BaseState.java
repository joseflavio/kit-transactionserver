package com.kit.lightserver.statemachine.states;

import com.kit.lightserver.statemachine.StateMachineMainContext;

abstract class BaseState {

    protected final StateMachineMainContext context;

    protected BaseState(final StateMachineMainContext context) {
        this.context = context;
    }// constructor

}// class
