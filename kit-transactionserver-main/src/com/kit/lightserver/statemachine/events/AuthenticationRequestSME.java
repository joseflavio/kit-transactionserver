package com.kit.lightserver.statemachine.events;

import com.kit.lightserver.domain.AuthenticationRequestTypeEnumSTY;
import com.kit.lightserver.services.types.InstallationIdSTY;
import com.kit.lightserver.statemachine.states.KitEventSME;


public final class AuthenticationRequestSME implements KitEventSME {

	private final String clientId;
	private final String password;
	private final AuthenticationRequestTypeEnumSTY authenticationRequestType;
	private final InstallationIdSTY installationIdSTY;

	public AuthenticationRequestSME(final String clientId, final String password,
			final AuthenticationRequestTypeEnumSTY authenticationRequestType, final InstallationIdSTY installationIdSTY) {

		this.clientId = clientId;
		this.password = password;
		this.authenticationRequestType = authenticationRequestType;
		this.installationIdSTY = installationIdSTY;

	}// constructor

	public String getUserClientId() {
		return clientId;
	}

	public String getPassword() {
		return password;
	}

	public AuthenticationRequestTypeEnumSTY getAuthenticationRequestType() {
		return authenticationRequestType;
	}

	public InstallationIdSTY getInstallationIdSTY() {
		return installationIdSTY;
	}

	@Override
	public String toString() {
		return "AuthenticationRequestSTY [clientId=" + clientId + ", password=" + password
				+ ", authenticationRequestType=" + authenticationRequestType + ", installationIdSTY="
				+ installationIdSTY + "]";
	}

}// class
