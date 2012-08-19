package com.kit.lightserver.domain.types;


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

    public ConhecimentoSTY(final int ktRowId, final String ktClientUserId, final boolean isReceived, final boolean isRead, final boolean isEdited,
            final String title, final String remetenteCNPJ, final String destinatarioNome) {

        this.template = TemplateEnumSTY.KNOWLEDGE_CONHECIMENTO;

        this.ktFormRowId = new FormConhecimentoRowIdSTY(ktRowId);
        this.ktClientId = ktClientUserId;

        this.isReceived = isReceived;
        this.isRead = isRead;
        this.isEdited = isEdited;

        this.title = title;
        this.remetenteCNPJ = remetenteCNPJ;
        this.destinatarioNome = destinatarioNome;

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

}// class
