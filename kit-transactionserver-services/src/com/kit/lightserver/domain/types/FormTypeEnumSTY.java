package com.kit.lightserver.domain.types;

public enum FormTypeEnumSTY {

    CO("CO"), // Conhecimento
    NF("NF"); // Notafiscal

    private final String formTypeCodeForDatabase;

    private FormTypeEnumSTY(final String formTypeCodeForDatabase) {
        this.formTypeCodeForDatabase = formTypeCodeForDatabase;
    }

    public String getFormTypeCodeForDatabase() {
        return formTypeCodeForDatabase;
    }

}// enum
