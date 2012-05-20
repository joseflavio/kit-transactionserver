package com.kit.lightserver.adapters.logger;

import kit.primitives.authentication.AuthenticationRequest;
import kit.primitives.authentication.AuthenticationResponse;
import kit.primitives.base.Primitive;
import kit.primitives.channel.ChannelNotification;
import kit.primitives.forms.FormOperation;

final class PrimitiveNameTranslator {

    static private final String ERROR_TYPE = "!ERROR!";

    public static String getName(final Primitive primitive) {

        final String nickname;
        if (primitive instanceof AuthenticationRequest) {
            final AuthenticationRequest castedPrimitive = (AuthenticationRequest) primitive;
            final String typeStr;
            if (castedPrimitive.type == AuthenticationRequest.RES_MANUAL_NEW_USER) {
                typeStr = "RES_MANUAL_NEW_USER(NEWLOGIN)";
            }
            else if (castedPrimitive.type == AuthenticationRequest.RES_MANUAL) {
                typeStr = "RES_MANUAL(PREVIOUS)";
            }
            else if (castedPrimitive.type == AuthenticationRequest.RES_AUTOMATIC) {
                typeStr = "RES_AUTOMATIC";
            }
            else if (castedPrimitive.type == AuthenticationRequest.UNDEFINED) {
                typeStr = "UNDEFINED";
            }
            else {
                typeStr = ERROR_TYPE;
            }
            nickname = "AuthenticationRequest[type=" + typeStr + "]";
        }
        else if (primitive instanceof AuthenticationResponse) {
            final AuthenticationResponse castedPrimitive = (AuthenticationResponse) primitive;
            final String typeStr;
            if (castedPrimitive.type == AuthenticationResponse.UNDEFINED) {
                typeStr = "UNDEFINED";
            }
            else if (castedPrimitive.type == AuthenticationResponse.INF_SUCCESS) {
                typeStr = "INF_SUCCESS";
            }
            else if (castedPrimitive.type == AuthenticationResponse.INF_FAILED) {
                typeStr = "INF_FAILED";
            }
            else if (castedPrimitive.type == AuthenticationResponse.CMD_AUTHENTICATION) {
                typeStr = "CMD_AUTHENTICATION(REQUEST)";
            }
            else if (castedPrimitive.type == AuthenticationResponse.INF_DATABASE_ERROR) {
                typeStr = "INF_DATABASE_ERROR";
            }
            else if (castedPrimitive.type == AuthenticationResponse.INF_SIMULTAENOUS_USER) {
                typeStr = "INF_SIMULTAENOUS_USER(CLIENTALREADYLOGGED)";
            }
            else if (castedPrimitive.type == AuthenticationResponse.INF_INEXISTENT_USER) {
                typeStr = "INF_INEXISTENT_USER";
            }
            else if (castedPrimitive.type == AuthenticationResponse.INF_INCORRECT_PASSWORD) {
                typeStr = "INF_INCORRECT_PASSWORD";
            }
            else if (castedPrimitive.type == AuthenticationResponse.CMD_LOGOUT) {
                typeStr = "CMD_LOGOUT";
            }
            else {
                typeStr = ERROR_TYPE;
            }
            nickname = "AuthenticationResponse[type=" + typeStr + "]";
        }
        else if (primitive instanceof ChannelNotification) {
            final ChannelNotification castedPrimitive = (ChannelNotification) primitive;
            final String typeStr;
            if(castedPrimitive.type == ChannelNotification.END_CHANNEL) {
                typeStr = "END_CHANNEL";
            }
            else
            if(castedPrimitive.type == ChannelNotification.ERROR_OUT_OF_MEMORY) {
                typeStr = "ERROR_OUT_OF_MEMORY";
            }
            else
            if(castedPrimitive.type == ChannelNotification.ERROR_PROTOCOL) {
                typeStr = "ERROR_PROTOCOL";
            }
            else
            if(castedPrimitive.type == ChannelNotification.ERROR_SERVER) {
                typeStr = "ERROR_SERVER";
            }
            else {
                typeStr = "(type=" + castedPrimitive.type + ")";
            }
            nickname = "FormOperation[type=" + typeStr + "]";
        }
        else if (primitive instanceof FormOperation) {
            final FormOperation castedPrimitive = (FormOperation) primitive;
            final String typeStr;
            if(castedPrimitive.type == FormOperation.RESET) {
                typeStr = "RESET";
            }
            else if(castedPrimitive.type == FormOperation.UPDATED_FORMS) {
                typeStr = "UPDATED_FORMS";
            }
            else if(castedPrimitive.type == FormOperation.UPDATED_FORMS_COMPLETE) {
                typeStr = "UPDATED_FORMS_COMPLETE";
            }
            else if(castedPrimitive.type == FormOperation.UPDATED_FORMS_CLEAR_FLAGS) {
                typeStr = "UPDATED_FORMS_CLEAR_FLAGS";
            }
            else if(castedPrimitive.type == FormOperation.ALL_NEW_FORMS) {
                typeStr = "ALL_NEW_FORMS";
            }
            else if(castedPrimitive.type == FormOperation.UPDATED_FORMS_PARTIAL) {
                typeStr = "UPDATED_FORMS_PARTIAL";
            }
            else if(castedPrimitive.type == FormOperation.UPDATED_FORMS_CLEAR_RECENT_FLAGS) {
                typeStr = "UPDATED_FORMS_CLEAR_RECENT_FLAGS";
            }
            else if(castedPrimitive.type == FormOperation.GET_STATUS) {
                typeStr = "GET_STATUS";
            }
            else if(castedPrimitive.type == FormOperation.CLIENT_FAILURE) {
                typeStr = "CLIENT_FAILURE";
            }
            else if(castedPrimitive.type == FormOperation.CLIENT_SUCCESS) {
                typeStr = "CLIENT_SUCCESS";
            }
            else {
                typeStr = ERROR_TYPE;
            }
            nickname = "FormOperation[type=" + typeStr + "]";
        }
        else {
            nickname = "(not defined)";
        }
        return nickname;
    }

}// class
