package com.kit.lightserver.types.response;



public final class SimpleRSTY implements ClientResponseRSTY {

    private final Type type;

    public SimpleRSTY(final Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    static public enum Type {
        ACTIVITY_GPS_REQUEST_LOG_LINE
    }

}
