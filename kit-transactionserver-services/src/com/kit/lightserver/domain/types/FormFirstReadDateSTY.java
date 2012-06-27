package com.kit.lightserver.domain.types;

import java.util.Date;

public final class FormFirstReadDateSTY {

    private final Date firstReadDate;

    public FormFirstReadDateSTY(final Date firstReadDate) {
        this.firstReadDate = new Date( firstReadDate.getTime() );
    }

    public Date getDate() {
        return firstReadDate;
    }

    @Override
    public String toString() {
        return "FormFirstReadDateSTY [firstReadDate=" + firstReadDate + "]";
    }

}// class
