package com.kit.lightserver.adapters.adapterout;

import kit.primitives.activity.ActivityPrimitive;
import kit.primitives.authentication.AuthenticationResponse;
import kit.primitives.base.Primitive;
import kit.primitives.channel.ChannelNotification;
import kit.primitives.channel.ChannelProgress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kit.lightserver.services.be.authentication.AuthenticationServiceResponse;
import com.kit.lightserver.services.be.authentication.AuthenticationServiceResponse.FailureType;
import com.kit.lightserver.types.response.AuthenticationResponseRSTY;
import com.kit.lightserver.types.response.ChannelNotificationEndConversationRSTY;
import com.kit.lightserver.types.response.ChannelNotificationServerErrorRSTY;
import com.kit.lightserver.types.response.ChannelProgressRSTY;
import com.kit.lightserver.types.response.ClientResponseRSTY;
import com.kit.lightserver.types.response.FormContentFullRSTY;
import com.kit.lightserver.types.response.FormOperationRSTY;
import com.kit.lightserver.types.response.SimpleRSTY;

final class AdoPrimiveConverter {

    static private final Logger LOGGER = LoggerFactory.getLogger(AdoPrimiveConverter.class);

    static public AdoConverterResult<Primitive> convert(final ClientResponseRSTY genericResponseRSTY) {

        final ConverterResult converterResult;
        if (genericResponseRSTY instanceof ChannelNotificationEndConversationRSTY) {
            final ChannelNotification channelNotification = new ChannelNotification();
            channelNotification.type = ChannelNotification.END_CHANNEL;
            final boolean success = true;
            converterResult = new ConverterResult(success, channelNotification);
        }
        else if (genericResponseRSTY instanceof AuthenticationResponseRSTY) {

            AuthenticationResponseRSTY responseRSTY = (AuthenticationResponseRSTY)genericResponseRSTY;
            AuthenticationServiceResponse authenticationResult = responseRSTY.getType();
            final byte primitiveType;
            if( authenticationResult.isSuccess() == true ) {
                if( authenticationResult.isMustReset() == true ) {
                    primitiveType = AuthenticationResponse.INF_SUCCESS;
                }
                else {
                    primitiveType = AuthenticationResponse.INF_SUCCESS;
                }
            }
            else {

                FailureType failureType = authenticationResult.getFailureType();
                if( failureType == AuthenticationServiceResponse.FailureType.FAILED_INVALID_PASSWORD ) {
                    primitiveType = AuthenticationResponse.INF_FAILED; // soon should be INF_INCORRECT_PASSWORD;
                }
                else
                if( failureType == AuthenticationServiceResponse.FailureType.FAILED_CLIENTID_DO_NOT_EXIST ) {
                    primitiveType = AuthenticationResponse.INF_INEXISTENT_USER;
                }
                else
                if( failureType == AuthenticationServiceResponse.FailureType.FAILED_NEWINSTALLATIONID_NO_AUTO_UPDATE ) {
                    primitiveType = AuthenticationResponse.CMD_LOGOUT;
                }
                else
                if( failureType == AuthenticationServiceResponse.FailureType.FAILED_DATABASE_ERROR ) {
                    primitiveType = AuthenticationResponse.INF_DATABASE_ERROR;
                }
                else {
                    primitiveType = AuthenticationResponse.UNDEFINED;
                }
            }

            final AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.type = primitiveType;
            converterResult = new ConverterResult(true, authenticationResponse);

        }
        else if (genericResponseRSTY instanceof ChannelNotificationServerErrorRSTY) {
            final ChannelNotification channelNotification = new ChannelNotification();
            channelNotification.type = ChannelNotification.ERROR_SERVER;
            final boolean success = true;
            converterResult = new ConverterResult(success, channelNotification);
        }
        else if (genericResponseRSTY instanceof ChannelProgressRSTY) {
            final ChannelProgressRSTY channelProgressRSTY = (ChannelProgressRSTY)genericResponseRSTY;
            final ChannelProgress channelProgress = new ChannelProgress();
            channelProgress.numberOfSteps = (short) channelProgressRSTY.getNumberOfSteps();
            final boolean success = true;
            converterResult = new ConverterResult(success, channelProgress);
        }
        else if (genericResponseRSTY instanceof FormOperationRSTY) {
            final FormOperationRSTY casted = (FormOperationRSTY) genericResponseRSTY;
            converterResult = AdoFormOperationConverter.converter(casted);
        }
        else if (genericResponseRSTY instanceof FormContentFullRSTY) {
            final FormContentFullRSTY formContentFullRSTY = (FormContentFullRSTY) genericResponseRSTY;
            converterResult = FormContentFullConverter.convertForm(formContentFullRSTY);
        }
        else if(genericResponseRSTY instanceof SimpleRSTY ) {
            final SimpleRSTY simple = (SimpleRSTY) genericResponseRSTY;

            switch ( simple.getType() ) {

            case ACTIVITY_GPS_REQ_LOG_LINE_LAST_ENTRIES:
                ActivityPrimitive morePrimitive = new ActivityPrimitive();
                morePrimitive.type = ActivityPrimitive.REQ_LOG_LINE;
                converterResult = new ConverterResult(true, morePrimitive);
                break;

            case ACTIVITY_GPS_CMD_DELETE_LAST_ENTRIES:
                ActivityPrimitive deletePrimitive = new ActivityPrimitive();
                deletePrimitive.type = ActivityPrimitive.CMD_DELETE;
                converterResult = new ConverterResult(true, deletePrimitive);
                break;

            default:
                converterResult = new ConverterResult();
                LOGGER.error("Unexpected type. type={}",  simple.getType());
                break;
            }


        }
        else {
            converterResult = new ConverterResult();
        }

        /*
         * In case it managed to convert
         */
        if( converterResult.isSuccess() ) {

            final boolean success = true;
            final Primitive primitiveToSend = converterResult.getPrimitiveToSend();
            final AdoConverterResult<Primitive> result = new AdoConverterResult<Primitive>(success, primitiveToSend);

            return result;

        } else {

            LOGGER.error("Unable to convert primitive. Error will be sent to client. primitiveSTY=" + genericResponseRSTY);
            final boolean success = false;
            final ChannelNotification channelNotification = new ChannelNotification();
            channelNotification.type = ChannelNotification.ERROR_SERVER;
            final AdoConverterResult<Primitive> result = new AdoConverterResult<Primitive>(success, channelNotification);

            return result;

        }


    }


}// class
