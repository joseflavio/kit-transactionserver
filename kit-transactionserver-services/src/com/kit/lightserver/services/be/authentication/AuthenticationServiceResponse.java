package com.kit.lightserver.services.be.authentication;

public enum AuthenticationServiceResponse {

    SUCCESS_NO_NEED_TO_RESET,
    SUCCESS_MUST_RESET,
    FAILED_CLIENTID_DO_NOT_EXIST,
    FAILED_INVALID_PASSWORD,
    FAILED_DATABASE_ERROR,
    FAILED_SIMULTANEUS_LOGIN,
    FAILED_NEWINSTALLATIONID_NO_AUTO_UPDATE,
    FAILED_UNEXPECTED_ERROR

}// enum
