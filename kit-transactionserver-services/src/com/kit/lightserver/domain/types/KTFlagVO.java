package com.kit.lightserver.domain.types;

public final class KTFlagVO {

    private final String flagname;
    private final Boolean value;

    public KTFlagVO(final String flagname, final boolean value) {
        this.flagname = flagname;
        this.value = Boolean.valueOf(value);
    }

    public KTFlagVO(final String flagname) {
        this.flagname = flagname;
        this.value = null;
    }

    public String getFlagname() {
        return flagname;
    }

    public Boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "KTFlagVO [flagname=" + flagname + ", value=" + value + "]";
    }

}// class
