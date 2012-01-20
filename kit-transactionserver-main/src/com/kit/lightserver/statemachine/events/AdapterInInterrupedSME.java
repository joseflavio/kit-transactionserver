package com.kit.lightserver.statemachine.events;

import com.kit.lightserver.statemachine.states.KitEventSME;


public final class AdapterInInterrupedSME implements KitEventSME {

    private final Exception exception;

    public AdapterInInterrupedSME(final Exception e) {
        this.exception = e;
    }

    @Override
    public String toString() {
        return "AdapterInInterrupedSME [exception=" + exception + "]";
    }

}// class
