package com.kit.lightserver.adapters.adapterout;

import kit.primitives.base.Primitive;

final class ConverterResult {

    private final boolean success;

    private final Primitive primitiveToSend;

    public ConverterResult(final boolean success, final Primitive primitiveToSend) {
        this.success = success;
        this.primitiveToSend = primitiveToSend;
    }// constructor

    public ConverterResult() {
        this.success = false;
        this.primitiveToSend = null;
    }

    public boolean isSuccess() {
        return success;
    }

    public Primitive getPrimitiveToSend() {
        return primitiveToSend;
    }

    @Override
    public String toString() {
        return "ConverterResult [success=" + success + ", primitiveToSend=" + primitiveToSend + "]";
    }

}// class
