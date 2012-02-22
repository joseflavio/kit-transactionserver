package com.kit.lightserver.services.be.authentication;

public final class AuthenticateQueryResult {

    private final Boolean userExists;
    private final String ktClientId;
    private final String ktPassword;
    private final Boolean ktDeveResetar;

    public AuthenticateQueryResult() {
        this.userExists = Boolean.FALSE;
        this.ktClientId = null;
        this.ktPassword = null;
        this.ktDeveResetar = null;
    }// constructor

    public AuthenticateQueryResult(final String ktClientId, final String ktPassword, final boolean ktDeveResetar) {
        this.userExists = Boolean.TRUE;
        this.ktClientId = ktClientId;
        this.ktPassword = ktPassword;
        this.ktDeveResetar = Boolean.valueOf(ktDeveResetar);
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

    public Boolean isKtDeveResetar() {
        return ktDeveResetar;
    }

    @Override
    public String toString() {
        return "AuthenticateQueryResult [userExists=" + userExists + ", ktClientId=" + ktClientId + ", ktPassword=" + ktPassword + ", ktDeveResetar="
                + ktDeveResetar + "]";
    }

}// class
