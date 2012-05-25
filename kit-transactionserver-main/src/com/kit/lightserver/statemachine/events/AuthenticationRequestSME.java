package com.kit.lightserver.statemachine.events;

import com.kit.lightserver.domain.types.AuthenticationRequestTypeEnumSTY;
import com.kit.lightserver.domain.types.InstallationIdAbVO;
import com.kit.lightserver.statemachine.states.KitEventSME;


public final class AuthenticationRequestSME implements KitEventSME {

	private final String clientId;
	private final String password;
	private final AuthenticationRequestTypeEnumSTY authenticationRequestType;
	private final InstallationIdAbVO installationIdSTY;

	public AuthenticationRequestSME(final String clientId, final String password,
			final AuthenticationRequestTypeEnumSTY authenticationRequestType, final InstallationIdAbVO installationIdSTY) {

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

	public InstallationIdAbVO getInstallationIdSTY() {
		return installationIdSTY;
	}

    @Override
    public String toString() {
        return "AuthenticationRequestSME [clientId=" + clientId + ", password=" + password + ", authenticationRequestType=" + authenticationRequestType
                + ", installationIdSTY=" + installationIdSTY + "]";
    }

}// class
