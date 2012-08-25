package com.fap.collections;

public final class SmartCollectionTransformResult<R> {

    private final boolean shouldAdd;

    private final R result;

    public SmartCollectionTransformResult() {
        this.shouldAdd = false;
        this.result = null;
    }

    public SmartCollectionTransformResult(final R result) {
        if( result == null ) { throw new NullPointerException(); }
        this.shouldAdd = true;
        this.result = result;
    }

    public boolean shouldAdd() {
        return shouldAdd;
    }

    public R getResult() {
        if( result == null ) { throw new NullPointerException(); }
        return result;
    }

}// class
