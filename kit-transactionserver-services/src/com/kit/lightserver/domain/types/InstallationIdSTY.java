package com.kit.lightserver.domain.types;

public final class InstallationIdSTY {

	private final long installationId1;
	private final long installationId2;

	public InstallationIdSTY(final long installationId1, final long installationId2) {
		this.installationId1 = installationId1;
		this.installationId2 = installationId2;
	}

	public long getInstallationId1() {
		return installationId1;
	}

	public long getInstallationId2() {
		return installationId2;
	}

	@Override
	public String toString() {
		return "InstallationIdSTY [installationId1=" + installationId1 + ", installationId2=" + installationId2 + "]";
	}

}// class
