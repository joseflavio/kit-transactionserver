package com.kit.lightserver.domain.types;

public final class FormUniqueIdSTY {

    private final FormIdSTY formId;

    private final TemplateEnumSTY formType;

    protected FormUniqueIdSTY(final TemplateEnumSTY formType, final FormIdSTY formId) {
        this.formType = formType;
        this.formId = formId;
    }

    public FormIdSTY getFormId() {
        return formId;
    }

    public TemplateEnumSTY getFormType() {
        return formType;
    }

    static public boolean isConhecimento(final FormUniqueIdSTY formId) {
        if( formId == null || formId.getFormType().equals(TemplateEnumSTY.KNOWLEDGE_CONHECIMENTO) == false ) {
            return false;
        }
        return true;
    }

    static public FormUniqueIdSTY newInstance(final TemplateEnumSTY type, final String idStr) {
        final FormIdSTY formId = FormIdSTY.newInstance(idStr);
        return new FormUniqueIdSTY(type, formId);
    }

}// class
