package com.kit.lightserver.statemachine.events;

import java.util.Date;

import com.kit.lightserver.statemachine.states.KitEventSME;

public final class FormContentFormReadSME implements KitEventSME {

    private final Date firstReadDate;

    public FormContentFormReadSME(final Date firstReadDate) {
        this.firstReadDate = firstReadDate;
    }

    @Override
    public String toString() {
        return "FormContentFormReadSME [firstReadDate=" + firstReadDate + "]";
    }

}// class
