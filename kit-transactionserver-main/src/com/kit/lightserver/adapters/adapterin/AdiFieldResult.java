package com.kit.lightserver.adapters.adapterin;

final class AdiFieldResult<T> {
    private final boolean exists;
    private final T value;
    AdiFieldResult() {
        this.exists = false;
        this.value = null;
    }
    AdiFieldResult(final T value) {
        this.exists = true;
        this.value = value;
    }
    public boolean isExists() {
        return exists;
    }
    public T getValue() {
        return value;
    }
    @Override
    public String toString() {
        return "AdiFieldResult [exists=" + exists + ", value=" + value + "]";
    }
}
