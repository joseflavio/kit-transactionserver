package com.kit.lightserver.types.response;


public final class AuthenticationResponseRSTY implements ClientResponseRSTY {

    private final Type type;

    public AuthenticationResponseRSTY(final Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "AuthenticationResponseFailedWrongPassowordRSTY [type=" + type + "]";
    }

    public enum Type {
        FAILED_INCORRECT_PASSWORD, FAILED_INEXISTENT_CLIENTID, FAILED_ALREADY_CONNECTED_OTHER_DEVICE, FAILED_DATABASE_ERROR, FAILED_UNEXPECTED_ERROR
    }

}// class
