package com.kit.lightserver.statemachine.states;

import com.kit.lightserver.statemachine.KitGeneralCTX;

abstract class KitTransactionalAbstractState {

    protected final KitGeneralCTX context;

    protected KitTransactionalAbstractState(final KitGeneralCTX context) {
        this.context = context;
    }// constructor

}// class
