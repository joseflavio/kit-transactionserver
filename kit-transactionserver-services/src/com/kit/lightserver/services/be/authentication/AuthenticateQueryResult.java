package com.kit.lightserver.services.be.authentication;

public final class AuthenticateQueryResult {

    private final Boolean userExists;
    private final String ktClientId;
    private final String ktPassword;
    private final Boolean ktDeveResetar;
    private final Boolean ktUsuarioConectado;

    public AuthenticateQueryResult() {
        this.userExists = false;
        this.ktClientId = null;
        this.ktPassword = null;
        this.ktDeveResetar = null;
        this.ktUsuarioConectado = null;
    }// constructor

    public AuthenticateQueryResult(final String ktClientId, final String ktPassword, final boolean ktDeveResetar, final boolean ktUsuarioConectado) {
        this.userExists = true;
        this.ktClientId = ktClientId;
        this.ktPassword = ktPassword;
        this.ktDeveResetar = ktDeveResetar;
        this.ktUsuarioConectado = ktUsuarioConectado;
    }// constructor

    public boolean isUserExists() {
        return userExists;
    }

    public String getKtClientId() {
        return ktClientId;
    }

    public String getKtPassword() {
        return ktPassword;
    }

    public boolean isKtDeveResetar() {
        return ktDeveResetar;
    }

    public boolean isKtUsuarioConectado() {
        return ktUsuarioConectado;
    }

    @Override
    public String toString() {
        return "AuthenticateQueryResult [userExists=" + userExists + ", ktClientId=" + ktClientId + ", ktPassword=" + ktPassword + ", ktDeveResetar="
                + ktDeveResetar + ", ktUsuarioConectado=" + ktUsuarioConectado + "]";
    }

}// class
