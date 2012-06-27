package com.kit.lightserver.services.be.forms;

public enum FormFlagEnum {

    RECEBIDO("Recebido");

    private String databaseColumnName;

    private FormFlagEnum(final String databaseColumnName) {
        this.databaseColumnName = databaseColumnName;
    }

    public String getDatabaseColumnName() {
        return databaseColumnName;
    }

}
