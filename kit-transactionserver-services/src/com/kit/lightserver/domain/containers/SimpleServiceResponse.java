package com.kit.lightserver.domain.containers;

public final class SimpleServiceResponse<T> {

    private final boolean valid;

    private final T validResult;

    public SimpleServiceResponse() {
        this.valid = false;
        this.validResult = null;
    }

    public SimpleServiceResponse(final T validResult) {
        this.valid = true;
        this.validResult = validResult;
    }

    public boolean isValid() {
        return valid;
    }

    public T getValidResult() {
        if (valid == false) {
            throw new RuntimeException("Invalid Result. It should not be accessed.");
        }
        return validResult;
    }

    @Override
    public String toString() {
        return "SimpleServiceResponse [valid=" + valid + ", validResult=" + validResult + "]";
    }

}// class
