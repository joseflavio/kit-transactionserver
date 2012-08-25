package com.kit.lightserver.domain.types;

public final class FormClientRowIdSTY {

    private final int ktFormRowId;
    private final TemplateEnumSTY formType;

    public FormClientRowIdSTY(final TemplateEnumSTY formType, final int ktRowId) {
        this.formType = formType;
        this.ktFormRowId = ktRowId;
    }

    public int getKtFormRowId() {
        return ktFormRowId;
    }

    public TemplateEnumSTY getFormType() {
        return formType;
    }

    @Override
    public String toString() {
        return "FormClientRowIdSTY [ktFormRowId=" + ktFormRowId + ", formType=" + formType + "]";
    }

    static public boolean isConhecimento(final FormClientRowIdSTY formClientRowId) {
        if( formClientRowId == null || formClientRowId.getFormType().equals(TemplateEnumSTY.KNOWLEDGE_CONHECIMENTO) == false ) {
            return false;
        }
        return true;
    }

    public static boolean isNotafiscal(final FormClientRowIdSTY formClientRowId) {
        if( formClientRowId == null || formClientRowId.getFormType().equals(TemplateEnumSTY.RECEIPT_NOTASFISCAIS) == false ) {
            return false;
        }
        return true;
    }

}// class
