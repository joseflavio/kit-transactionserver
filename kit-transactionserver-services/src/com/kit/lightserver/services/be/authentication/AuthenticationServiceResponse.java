package com.kit.lightserver.services.be.authentication;


public final class AuthenticationServiceResponse {

    static public AuthenticationServiceResponse getFailedInstance(final FailureType failureType) {
        return new AuthenticationServiceResponse(failureType);
    }

    static public AuthenticationServiceResponse getSuccessInstance(final boolean mustReset, final boolean isGpsEnabled) {
        return new AuthenticationServiceResponse(mustReset, isGpsEnabled);
    }

    static public enum FailureType {
        FAILED_CLIENTID_DO_NOT_EXIST,
        FAILED_INVALID_PASSWORD,
        FAILED_DATABASE_ERROR,
        FAILED_SIMULTANEUS_LOGIN,
        FAILED_NEWINSTALLATIONID_NO_AUTO_UPDATE,
        FAILED_UNEXPECTED_ERROR
    }

    private final boolean success;
    private final boolean mustReset;
    private final boolean gpsEnabled;
    private final FailureType failureType;

    private AuthenticationServiceResponse(final FailureType failureType) {
        this.success = false;
        this.mustReset = false;
        this.gpsEnabled = false;
        this.failureType = failureType;
    }

    public AuthenticationServiceResponse(final boolean mustReset, final boolean isGpsEnabled) {
        this.success = true;
        this.mustReset = mustReset;
        this.gpsEnabled = isGpsEnabled;
        this.failureType = null;
    }

    public boolean isSuccess() {
        return success;
    }

    public FailureType getFailureType() {
        if( success == true ) { throw new UnsupportedOperationException(); }
        return failureType;
    }

    public boolean isMustReset() {
        if( success == false ) { throw new UnsupportedOperationException(); }
        return mustReset;
    }

    public boolean isGpsEnabled() {
        if( success == false ) { throw new UnsupportedOperationException(); }
        return gpsEnabled;
    }

    @Override
    public String toString() {
        return "AuthenticationServiceResponse [success=" + success + ", mustReset=" + mustReset + ", gpsEnabled=" + gpsEnabled + ", failureType=" + failureType
                + "]";
    }

}
