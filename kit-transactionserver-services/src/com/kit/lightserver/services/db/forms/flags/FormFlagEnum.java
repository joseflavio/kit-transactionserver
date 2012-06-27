package com.kit.lightserver.services.db.forms.flags;

enum FormFlagEnum {

    RECEBIDO("Recebido"),
    LIDO("Lido"),
    EDITADO("Editado");

    private String databaseColumnName;

    private FormFlagEnum(final String databaseColumnName) {
        this.databaseColumnName = databaseColumnName;
    }

    public String getDatabaseColumnName() {
        return databaseColumnName;
    }

}// enum
