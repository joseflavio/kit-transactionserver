package com.kit.lightserver.domain.types;

public abstract class FormRowIdSTY {

    protected final int ktFormRowId;

    public FormRowIdSTY(final int ktRowId) {
        this.ktFormRowId = ktRowId;
    }

    public int getKtFormRowId() {
        return ktFormRowId;
    }

}// class
