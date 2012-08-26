package com.kit.lightserver.domain.types;

public final class FormIdSTY {

    private final String value;

    private FormIdSTY(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "FormIdSTY [value=" + value + "]";
    }

    static public FormIdSTY newInstance(final String value) {
        if( value == null ) {
            throw new RuntimeException("The FormID can not be null.");
        }
        return new FormIdSTY(value);
    }

}// class
