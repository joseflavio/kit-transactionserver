package com.kit.lightserver.domain.types;

import java.util.Date;

import com.kit.lightserver.domain.util.DateCopier;

public final class FormFirstReadDateSTY {

    private final Date firstReadDate;

    public FormFirstReadDateSTY(final Date firstReadDate) {
        this.firstReadDate = DateCopier.newInstance( firstReadDate );
    }

    public Date getDate() {
        return DateCopier.newInstance(  firstReadDate );
    }

    @Override
    public String toString() {
        return "FormFirstReadDateSTY [firstReadDate=" + firstReadDate + "]";
    }

}// class
