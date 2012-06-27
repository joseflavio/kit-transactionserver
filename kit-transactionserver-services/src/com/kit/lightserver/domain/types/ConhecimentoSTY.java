package com.kit.lightserver.domain.types;

import java.util.Date;

public final class ConhecimentoSTY extends FormSTY {

    private final TemplateEnumSTY template;
    private final FormConhecimentoRowIdSTY ktFormRowId;
    private final String ktClientId;

    private final boolean isReceived;
    private final boolean isRead;
    private final boolean isEdited;

    private final String title;
    private final String remetenteCNPJ; // e.g: 03762480000116
    private final String destinatarioNome;

    private final Date dataEntrega; // TODO: REMOVER?
    private final StatusEntregaEnumSTY statusEntrega;

    public ConhecimentoSTY(final int ktRowId, final String ktClientUserId, final boolean isReceived, final boolean isRead, final boolean isEdited,
            final String title, final String remetenteCNPJ, final String destinatarioNome, final Date dataEntrega, final StatusEntregaEnumSTY statusEntrega) {

        this.template = TemplateEnumSTY.KNOWLEDGE_CONHECIMENTO;

        this.ktFormRowId = new FormConhecimentoRowIdSTY(ktRowId);
        this.ktClientId = ktClientUserId;

        this.isReceived = isReceived;
        this.isRead = isRead;
        this.isEdited = isEdited;

        this.title = title;
        this.remetenteCNPJ = remetenteCNPJ;
        this.destinatarioNome = destinatarioNome;
        this.dataEntrega = dataEntrega;
        this.statusEntrega = statusEntrega;

    }// constructor

    public TemplateEnumSTY getTemplate() {
        return template;
    }

    public FormConhecimentoRowIdSTY getKtFormRowId() {
        return ktFormRowId;
    }

    public String getKtClientUserId() {
        return ktClientId;
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

    public String getTitle() {
        return title;
    }

    public String getRemetenteCNPJ() {
        return remetenteCNPJ;
    }

    public String getDestinatarioNome() {
        return destinatarioNome;
    }

    public StatusEntregaEnumSTY getStatusEntrega() {
        return statusEntrega;
    }

    public Date getDataEntrega() {
        return dataEntrega;
    }

    @Override
    public String toString() {
        return "ConhecimentoSTY [template=" + template + ", ktRowId=" + ktFormRowId + ", ktClientId=" + ktClientId + ", isReceived=" + isReceived + ", isRead="
                + isRead + ", isEdited=" + isEdited + ", title=" + title + ", remetenteCNPJ=" + remetenteCNPJ + ", destinatarioNome=" + destinatarioNome
                + ", dataEntrega=" + dataEntrega + ", statusEntrega=" + statusEntrega + "]";
    }

}// class
