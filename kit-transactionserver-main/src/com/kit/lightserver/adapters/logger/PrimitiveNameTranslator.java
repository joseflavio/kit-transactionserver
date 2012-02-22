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
            if (castedPrimitive.type == AuthenticationRequest.NEWLOGIN) {
                typeStr = "NEWLOGIN";
            }
            else if (castedPrimitive.type == AuthenticationRequest.PREVIOUS) {
                typeStr = "PREVIOUS";
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
            else if (castedPrimitive.type == AuthenticationResponse.SUCCESS) {
                typeStr = "SUCCESS";
            }
            else if (castedPrimitive.type == AuthenticationResponse.FAILED) {
                typeStr = "FAILED";
            }
            else if (castedPrimitive.type == AuthenticationResponse.REQUEST) {
                typeStr = "REQUEST";
            }
            else if (castedPrimitive.type == AuthenticationResponse.DATABASEERROR) {
                typeStr = "DATABASEERROR";
            }
            else if (castedPrimitive.type == AuthenticationResponse.CLIENTALREADYLOGGED) {
                typeStr = "CLIENTALREADYLOGGED";
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
