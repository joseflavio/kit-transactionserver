package com.fap.framework.db;


public final class SelectQueryResult<T> {

	private final boolean success;

	private final T result;

	public SelectQueryResult(final T result) {
		this.success = true;
		this.result = result;
	}// constructor

	public SelectQueryResult() {
		this.success = false;
		this.result = null;
	}// constructor

	public boolean isQuerySuccessful() {
		return success;
	}

	public T getResult() {

		if( success == false ) {
			throw new RuntimeException("Result not available.");
		}

		if( result == null ) {
			throw new NullPointerException();
		}

		return result;

	}

    @Override
    public String toString() {
        return "QueryResultContainer [success=" + success + ", result=" + result + "]";
    }

}// class
