package com.kit.lightserver.domain.types;

import java.util.Date;

public final class NotafiscalSTY extends FormSTY {

    private final TemplateEnumSTY template;
    private final int ktRowId;

    private final boolean isReceived;
    private final boolean isRead;
    private final boolean isEdited;

    private final int parentKnowledgeRowId;
    private final String title;

    private final Date dataEntrega;
    private final StatusEntregaEnumSTY statusEntrega;

    public NotafiscalSTY(final int ktRowId, final boolean isReceived, final boolean isRead, final boolean isEdited, final int parentKnowledgeRowId,
            final String title, final Date dataEntrega, final StatusEntregaEnumSTY statusEntrega) {

        this.template = TemplateEnumSTY.RECEIPT_NOTASFISCAIS;
        this.ktRowId = ktRowId;

        this.isReceived = isReceived;
        this.isRead = isRead;
        this.isEdited = isEdited;

        this.parentKnowledgeRowId = parentKnowledgeRowId;
        this.title = title;

        this.dataEntrega = dataEntrega;
        this.statusEntrega = statusEntrega;

    }// constructor

    public TemplateEnumSTY getTemplate() {
        return template;
    }

    public int getKtRowId() {
        return ktRowId;
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

    public int getParentKnowledgeRowId() {
        return parentKnowledgeRowId;
    }

    public String getTitle() {
        return title;
    }

    public Date getDataEntrega() {
        return dataEntrega;
    }

    public StatusEntregaEnumSTY getStatusEntrega() {
        return statusEntrega;
    }

    @Override
    public String toString() {
        return "NotafiscalSTY [template=" + template + ", ktRowId=" + ktRowId + ", isReceived=" + isReceived + ", isRead=" + isRead + ", isEdited=" + isEdited
                + ", parentKnowledgeRowId=" + parentKnowledgeRowId + ", title=" + title + ", dataEntrega=" + dataEntrega + ", statusEntrega=" + statusEntrega
                + "]";
    }

}// class
