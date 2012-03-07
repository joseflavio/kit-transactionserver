package com.fap.framework.db.util;

public final class MsSqlBitConverter {

    static public boolean convert(final int value) {
        if (value == 0) {
            return false;
        }
        else if (value == 1) {
            return true;
        }
        else {
            throw new RuntimeException("Invalid value for a MsSql bit. value=" + value);
        }
    }

}// class
