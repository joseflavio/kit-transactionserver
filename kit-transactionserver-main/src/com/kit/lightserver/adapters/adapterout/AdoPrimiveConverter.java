package com.kit.lightserver.adapters.adapterout;

import kit.primitives.authentication.AuthenticationResponse;
import kit.primitives.base.Primitive;
import kit.primitives.channel.ChannelNotification;
import kit.primitives.channel.ChannelProgress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kit.lightserver.services.be.authentication.AuthenticationServiceResponse;
import com.kit.lightserver.types.response.AuthenticationResponseRSTY;
import com.kit.lightserver.types.response.ChannelNotificationEndConversationRSTY;
import com.kit.lightserver.types.response.ChannelNotificationServerErrorRSTY;
import com.kit.lightserver.types.response.ChannelProgressRSTY;
import com.kit.lightserver.types.response.ClientResponseRSTY;
import com.kit.lightserver.types.response.FormContentFullRSTY;
import com.kit.lightserver.types.response.FormOperationRSTY;

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

            final AuthenticationResponseRSTY responseRSTY = (AuthenticationResponseRSTY)genericResponseRSTY;
            final byte primitiveType;
            if( responseRSTY.getType() == AuthenticationServiceResponse.SUCCESS_MUST_RESET ) {
                primitiveType = AuthenticationResponse.INF_SUCCESS;
            }
            else
            if( responseRSTY.getType() == AuthenticationServiceResponse.SUCCESS_NO_NEED_TO_RESET ) {
                primitiveType = AuthenticationResponse.INF_SUCCESS;
            }
            else
            if( responseRSTY.getType() == AuthenticationServiceResponse.FAILED_INVALID_PASSWORD ) {
                primitiveType = AuthenticationResponse.INF_FAILED; // soon should be INF_INCORRECT_PASSWORD;
            }
            else
            if( responseRSTY.getType() == AuthenticationServiceResponse.FAILED_CLIENTID_DO_NOT_EXIST ) {
                primitiveType = AuthenticationResponse.INF_INEXISTENT_USER;
            }
            else
            if( responseRSTY.getType() == AuthenticationServiceResponse.FAILED_NEWINSTALLATIONID_NO_AUTO_UPDATE ) {
                primitiveType = AuthenticationResponse.CMD_LOGOUT;
            }
            else
            if( responseRSTY.getType() == AuthenticationServiceResponse.FAILED_DATABASE_ERROR ) {
                primitiveType = AuthenticationResponse.INF_DATABASE_ERROR;
            }
            else {
                primitiveType = AuthenticationResponse.UNDEFINED;
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
