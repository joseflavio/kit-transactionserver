package com.kit.lightserver.domain.types;

public final class ConhecimentoIdSTY {

    private final int ktRowId;

    public ConhecimentoIdSTY(final int ktRowId) {
        this.ktRowId = ktRowId;
    }

    public int getKtRowId() {
        return ktRowId;
    }

    @Override
    public String toString() {
        return "ConhecimentoIdSTY [ktRowId=" + ktRowId + "]";
    }

}// class
