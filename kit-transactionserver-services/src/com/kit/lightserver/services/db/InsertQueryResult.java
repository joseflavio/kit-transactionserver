package com.kit.lightserver.services.db;

public final class InsertQueryResult {

    private final boolean querySuccessfullyExecuted;

    private final Integer rowsInserted;

    public InsertQueryResult() {
        this.querySuccessfullyExecuted = false;
        this.rowsInserted = null;
    }

    public InsertQueryResult(final int rowsUpdated) {
        this.querySuccessfullyExecuted = true;
        this.rowsInserted = rowsUpdated;
    }

    public Integer getRowsInserted() {
        return rowsInserted;
    }

    public boolean isQuerySuccessfullyExecuted() {
        return querySuccessfullyExecuted;
    }

    @Override
    public String toString() {
        return "InsertQueryResult [querySuccessfullyExecuted=" + querySuccessfullyExecuted + ", rowsInserted=" + rowsInserted + "]";
    }


}// class
