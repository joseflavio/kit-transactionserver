package com.kit.lightserver.adapters.adapterin;

import com.kit.lightserver.statemachine.states.KitEventSME;

final class ReceivedPrimitiveConverterResult<T extends KitEventSME> {

    private final boolean success;

    private final T event;

    public ReceivedPrimitiveConverterResult(final boolean success, final T event) {
        this.success = success;
        this.event = event;
    }// constructor

    public ReceivedPrimitiveConverterResult() {
        this.success = false;
        this.event = null;
    }// constructor

    public boolean isSuccess() {
        return success;
    }

    public KitEventSME getEvent() {
        return event;
    }

    @Override
    public String toString() {
        return "ReceivedPrimitiveConverterResult [success=" + success + ", event=" + event + "]";
    }


}// class
