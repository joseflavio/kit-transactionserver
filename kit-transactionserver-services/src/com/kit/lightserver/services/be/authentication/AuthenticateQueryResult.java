package com.kit.lightserver.services.be.authentication;

public final class AuthenticateQueryResult {

    private final Boolean userExists;
    private final String ktClientId;
    private final String ktPassword;

    public AuthenticateQueryResult() {
        this.userExists = Boolean.FALSE;
        this.ktClientId = null;
        this.ktPassword = null;
    }// constructor

    public AuthenticateQueryResult(final String ktClientId, final String ktPassword) {
        this.userExists = Boolean.TRUE;
        this.ktClientId = ktClientId;
        this.ktPassword = ktPassword;
    }// constructor

    public Boolean isUserExists() {
        return userExists;
    }

    public String getKtClientId() {
        return ktClientId;
    }

    public String getKtPassword() {
        return ktPassword;
    }

    @Override
    public String toString() {
        return "AuthenticateQueryResult [userExists=" + userExists + ", ktClientId=" + ktClientId + ", ktPassword=" + ktPassword + "]";
    }

}// class
