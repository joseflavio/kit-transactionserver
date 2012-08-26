package com.kit.lightserver.domain.types;


public final class NotafiscalSTY extends FormSTY {

    private final FormUniqueIdSTY formUniqueId;
    private final FormClientRowIdSTY formClientRowId;
    private final FormUniqueIdSTY parentFormId;
    private final FormFlagsSTY formFlags;
    private final String title;

    public NotafiscalSTY(final FormIdSTY formId, final int ktRowId, final FormFlagsSTY formFlags, final FormUniqueIdSTY parentFormId, final String title) {

        this.formUniqueId = new FormUniqueIdSTY(TemplateEnumSTY.NF, formId);
        this.formClientRowId = new FormClientRowIdSTY(TemplateEnumSTY.NF, ktRowId);
        this.formFlags = formFlags;
        this.parentFormId = parentFormId;
        this.title = title;

    }// constructor

    public FormUniqueIdSTY getFormUniqueId() {
        return formUniqueId;
    }

    public FormClientRowIdSTY getFormClientRowId() {
        return formClientRowId;
    }

    public FormFlagsSTY getFormFlags() {
        return formFlags;
    }

    public FormUniqueIdSTY getParentFormId() {
        return parentFormId;
    }

    public String getTitle() {
        return title;
    }

}// class
