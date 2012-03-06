package com.kit.lightserver.domain.types;

abstract class AbstractFormIdSTY {

    protected final int ktRowId;

    public AbstractFormIdSTY(final int ktRowId) {
        this.ktRowId = ktRowId;
    }

    public int getKtRowId() {
        return ktRowId;
    }

}// class
