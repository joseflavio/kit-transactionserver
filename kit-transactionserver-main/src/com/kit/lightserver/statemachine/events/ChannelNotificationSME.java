package com.kit.lightserver.statemachine.events;

import com.kit.lightserver.statemachine.states.KitEventSME;

public final class ChannelNotificationSME implements KitEventSME {

    private final Type type;

    public ChannelNotificationSME(final Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "ChannelNotificationSME [type=" + type + "]";
    }

    public enum Type {
        END_CHANNEL, ERROR_PROTOCOL
    }

}// class
