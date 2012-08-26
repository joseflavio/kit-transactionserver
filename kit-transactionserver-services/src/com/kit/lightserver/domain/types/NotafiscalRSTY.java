package com.kit.lightserver.domain.types;


public final class NotafiscalRSTY {

    private final TemplateEnumSTY formType = TemplateEnumSTY.NF;

    private final FormClientRowIdSTY formClientRowIdSTY;

    private final FormClientRowIdSTY parentFormClientRowIdSTY;

    private final FormFlagsSTY formFlagsSTY;

    private final String title;

    public NotafiscalRSTY(final FormClientRowIdSTY formClientRowIdSTY, final FormClientRowIdSTY parentFormClientRowIdSTY, final FormFlagsSTY formFlagsSTY,
            final String title) {

        this.formClientRowIdSTY = formClientRowIdSTY;
        this.parentFormClientRowIdSTY = parentFormClientRowIdSTY;
        this.formFlagsSTY = formFlagsSTY;
        this.title = title;

    }

    public TemplateEnumSTY getFormType() {
        return formType;
    }

    public FormClientRowIdSTY getFormClientRowId() {
        return formClientRowIdSTY;
    }

    public FormClientRowIdSTY getParentFormClientKtRowId() {
        return parentFormClientRowIdSTY;
    }

    public FormFlagsSTY getFormFlagsSTY() {
        return formFlagsSTY;
    }

    public String getTitle() {
        return title;
    }


}// class
