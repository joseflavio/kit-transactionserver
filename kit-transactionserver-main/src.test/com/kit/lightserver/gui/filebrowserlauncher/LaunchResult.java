package com.kit.lightserver.gui.filebrowserlauncher;

public final class LaunchResult {

	static final public LaunchResult SUCCESS = new LaunchResult();

	static public LaunchResult getError(final String errorMessage) {
		return new LaunchResult(errorMessage);
	}

	private final boolean success;

	private final String errorMessage;

	private LaunchResult() {
		this.success = true;
		this.errorMessage = null;
	}

	private LaunchResult(final String errorMessage) {
		this.success = false;
		this.errorMessage = errorMessage;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}// class
