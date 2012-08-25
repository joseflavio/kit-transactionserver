package com.kit.lightserver.domain.types;


public final class ConhecimentoSTY extends FormSTY {

    private final FormUniqueIdSTY formUniqueId;
    private final FormClientRowIdSTY formClientRowId;

    private final String ktClientUserId;
    private final FormFlagsSTY formFlags;

    private final String title;
    private final String remetenteCNPJ; // e.g: 03762480000116
    private final String destinatarioNome;

    public ConhecimentoSTY(final FormIdSTY formId, final int ktRowId, final String ktClientUserId, final FormFlagsSTY formFlags, final String title,
            final String remetenteCNPJ, final String destinatarioNome) {

        this.formUniqueId = new FormUniqueIdSTY( TemplateEnumSTY.KNOWLEDGE_CONHECIMENTO, formId );
        this.formClientRowId = new FormClientRowIdSTY(TemplateEnumSTY.KNOWLEDGE_CONHECIMENTO, ktRowId);

        this.ktClientUserId = ktClientUserId;

        this.formFlags = formFlags;

        this.title = title;
        this.remetenteCNPJ = remetenteCNPJ;
        this.destinatarioNome = destinatarioNome;

    }// constructor

    public FormUniqueIdSTY getFormUniqueId() {
        return formUniqueId;
    }

    public FormClientRowIdSTY getFormClientRowId() {
        return formClientRowId;
    }

    public String getKtClientUserId() {
        return ktClientUserId;
    }

    public FormFlagsSTY getFormFlags() {
        return formFlags;
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
