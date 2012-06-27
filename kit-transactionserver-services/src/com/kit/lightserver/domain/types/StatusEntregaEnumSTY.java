package com.kit.lightserver.domain.types;

public enum StatusEntregaEnumSTY {

    AN("AN"), // AN_AINDA_NAO_ENTREGUE
    SU("SU"), // SU - SUCCESS
    E1("E1"), E2("E2"), E3("E3");

    private final String databaseCode;

    private StatusEntregaEnumSTY(final String databaseCode) {
        this.databaseCode = databaseCode;
    }

    public String getDatabaseCode() {
        return databaseCode;
    }

}// enum
