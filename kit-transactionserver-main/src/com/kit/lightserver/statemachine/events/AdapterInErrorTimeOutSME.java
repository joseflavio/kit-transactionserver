package com.kit.lightserver.statemachine.events;

import com.kit.lightserver.statemachine.states.KitEventSME;


public final class AdapterInErrorTimeOutSME implements KitEventSME {

    private final long timeOutInMillis;

    public AdapterInErrorTimeOutSME(final long timeOutInMillis) {
        this.timeOutInMillis = timeOutInMillis;
    }

    @Override
    public String toString() {
        return "AdapterInErrorTimeOutSME [timeOutInMillis=" + timeOutInMillis + "]";
    }

}// class
