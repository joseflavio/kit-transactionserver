package com.kit.lightserver.services.db.dbd;

enum FormFlagEnum {

    RECEBIDO("KTFlagRecebido"),
    LIDO("KTFlagLido"),
    EDITADO("KTFlagEditado");

    private String databaseColumnName;

    private FormFlagEnum(final String databaseColumnName) {
        this.databaseColumnName = databaseColumnName;
    }

    public String getDbFlagColumnName() {
        return databaseColumnName;
    }

}// enum
