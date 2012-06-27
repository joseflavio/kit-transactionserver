package com.kit.lightserver.domain.types;

public abstract class AbstractFormRowIdSTY {

    protected final int ktFormRowId;

    public AbstractFormRowIdSTY(final int ktRowId) {
        this.ktFormRowId = ktRowId;
    }

    public int getKtFormRowId() {
        return ktFormRowId;
    }

}// class
