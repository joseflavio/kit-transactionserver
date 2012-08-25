package com.kit.lightserver.domain.types;

import java.util.Date;

import com.kit.lightserver.domain.util.DateCopier;

public final class NotafiscalSTY extends FormSTY {

    private final FormUniqueIdSTY formUniqueId;
    private final FormClientRowIdSTY formClientRowId;

    private final boolean isReceived;
    private final boolean isRead;
    private final boolean isEdited;

    private final FormUniqueIdSTY parentFormId;
    private final String title;

    private final Date dataEntrega;
    private final StatusEntregaEnumSTY statusEntrega;

    public NotafiscalSTY(final FormIdSTY formId, final int ktRowId, final boolean isReceived, final boolean isRead, final boolean isEdited,
            final FormUniqueIdSTY parentFormId, final String title, final Date dataEntrega, final StatusEntregaEnumSTY statusEntrega) {

        this.formUniqueId = new FormUniqueIdSTY(TemplateEnumSTY.RECEIPT_NOTASFISCAIS, formId);
        this.formClientRowId = new FormClientRowIdSTY(TemplateEnumSTY.RECEIPT_NOTASFISCAIS, ktRowId);

        this.isReceived = isReceived;
        this.isRead = isRead;
        this.isEdited = isEdited;

        this.parentFormId = parentFormId;
        this.title = title;

        this.dataEntrega = DateCopier.newInstance( dataEntrega );
        this.statusEntrega = statusEntrega;

    }// constructor

    public FormUniqueIdSTY getFormUniqueId() {
        return formUniqueId;
    }

    public FormClientRowIdSTY getFormClientRowId() {
        return formClientRowId;
    }

    public boolean isReceived() {
        return isReceived;
    }

    public boolean isRead() {
        return isRead;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public FormUniqueIdSTY getParentFormId() {
        return parentFormId;
    }

    public String getTitle() {
        return title;
    }

    public Date getDataEntrega() {
        return DateCopier.newInstance( dataEntrega );
    }

    public StatusEntregaEnumSTY getStatusEntrega() {
        return statusEntrega;
    }

}// class
