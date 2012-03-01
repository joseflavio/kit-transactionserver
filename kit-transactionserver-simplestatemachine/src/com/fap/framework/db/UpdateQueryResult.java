package com.fap.framework.db;

public final class UpdateQueryResult {

    private final boolean querySuccessfullyExecuted;

    private final Integer rowsUpdated;

    public UpdateQueryResult() {
        this.querySuccessfullyExecuted = false;
        this.rowsUpdated = null;
    }

    public UpdateQueryResult(final int rowsUpdated) {
        this.querySuccessfullyExecuted = true;
        this.rowsUpdated = rowsUpdated;
    }

    public int getRowsUpdated() {
        return rowsUpdated;
    }

    public boolean isUpdateQuerySuccessful() {
        return querySuccessfullyExecuted;
    }

    @Override
    public String toString() {
        return "SimpleUpdateResult [querySuccessfullyExecuted=" + querySuccessfullyExecuted + ", rowsUpdated=" + rowsUpdated + "]";
    }

}// class
