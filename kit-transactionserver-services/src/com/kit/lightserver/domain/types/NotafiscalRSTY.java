package com.kit.lightserver.domain.types;

import java.util.Date;

public final class NotafiscalRSTY {

    private final TemplateEnumSTY formType = TemplateEnumSTY.RECEIPT_NOTASFISCAIS;

    private final FormClientRowIdSTY formClientRowIdSTY;

    private final FormClientRowIdSTY parentFormClientRowIdSTY;

    private final FormFlagsSTY formFlagsSTY;

    private final String title;

    private final Date dataEntrega;

    public NotafiscalRSTY(final FormClientRowIdSTY formClientRowIdSTY, final FormClientRowIdSTY parentFormClientRowIdSTY, final FormFlagsSTY formFlagsSTY,
            final String title, final Date dataEntrega) {

        this.formClientRowIdSTY = formClientRowIdSTY;
        this.parentFormClientRowIdSTY = parentFormClientRowIdSTY;
        this.formFlagsSTY = formFlagsSTY;
        this.title = title;
        this.dataEntrega = new Date(dataEntrega.getTime());

    }

    public TemplateEnumSTY getFormType() {
        return formType;
    }

    public FormClientRowIdSTY getFormClientRowId() {
        return formClientRowIdSTY;
    }

    public FormClientRowIdSTY getParentFormClientKtRowId() {
        return parentFormClientRowIdSTY;
    }

    public FormFlagsSTY getFormFlagsSTY() {
        return formFlagsSTY;
    }

    public String getTitle() {
        return title;
    }

    public Date getDataEntrega() {
        return new Date(dataEntrega.getTime());
    }

}// class
