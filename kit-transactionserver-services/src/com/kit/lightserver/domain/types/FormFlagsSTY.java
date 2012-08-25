package com.kit.lightserver.domain.types;

public final class FormFlagsSTY {

    private final boolean isReceived;
    private final boolean isRead;
    private final boolean isEdited;

    public FormFlagsSTY(final boolean isReceived, final boolean isRead, final boolean isEdited) {
        this.isReceived = isReceived;
        this.isRead = isRead;
        this.isEdited = isEdited;
    }

    public boolean isReceived() {
        return isReceived;
    }

    public boolean isRead() {
        return isRead;
    }

    public boolean isEdited() {
        return isEdited;
    }

}// class
