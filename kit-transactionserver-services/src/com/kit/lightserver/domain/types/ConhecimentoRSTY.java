package com.kit.lightserver.domain.types;

public final class ConhecimentoRSTY {

    private final TemplateEnumSTY formType = TemplateEnumSTY.CO;

    private final FormClientRowIdSTY formClientRowIdSTY;

    private final FormFlagsSTY formFlagsSTY;

    private final String title;

    private final String remetenteCNPJ;

    private final String destinatarioNome;

    public ConhecimentoRSTY(final FormClientRowIdSTY formClientRowIdSTY, final FormFlagsSTY formFlagsSTY, final String title, final String remetenteCNPJ,
            final String destinatarioNome) {

        this.formClientRowIdSTY = formClientRowIdSTY;
        this.formFlagsSTY = formFlagsSTY;
        this.title = title;
        this.remetenteCNPJ = remetenteCNPJ;
        this.destinatarioNome = destinatarioNome;

    }

    public FormClientRowIdSTY getFormClientRowId() {
        return formClientRowIdSTY;
    }

    public TemplateEnumSTY getFormType() {
        return formType;
    }

    public FormFlagsSTY getFormFlags() {
        return formFlagsSTY;
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
