package com.kit.lightserver.services.be.authentication;

public final class AuthenticateQueryResult {

    private final boolean userExists;
    private final String userClientId;
    private final String password;

    public AuthenticateQueryResult() {
        this.userExists = false;
        this.userClientId = null;
        this.password = null;
    }// constructor

    public AuthenticateQueryResult(final String ktClientId, final String ktPassword) {
        this.userExists = true;
        this.userClientId = ktClientId;
        this.password = ktPassword;
    }// constructor

    public boolean isUserExists() {
        return userExists;
    }

    public String getClientUserId() {
        return userClientId;
    }

    public String getPassword() {
        assert( this.userExists == true );
        return password;
    }

    @Override
    public String toString() {
        return "AuthenticateQueryResult [userExists=" + userExists + ", userClientId=" + userClientId + ", password=" + password + "]";
    }

}// class
