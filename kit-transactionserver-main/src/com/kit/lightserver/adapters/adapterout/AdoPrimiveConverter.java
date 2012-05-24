package com.kit.lightserver.adapters.adapterout;

import kit.primitives.authentication.AuthenticationResponse;
import kit.primitives.base.Primitive;
import kit.primitives.channel.ChannelNotification;
import kit.primitives.channel.ChannelProgress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kit.lightserver.types.response.AuthenticationResponseRSTY;
import com.kit.lightserver.types.response.AuthenticationResponseSuccessRSTY;
import com.kit.lightserver.types.response.ChannelNotificationEndConversationRSTY;
import com.kit.lightserver.types.response.ChannelNotificationServerErrorRSTY;
import com.kit.lightserver.types.response.ChannelProgressRSTY;
import com.kit.lightserver.types.response.ClientResponseRSTY;
import com.kit.lightserver.types.response.FormContentFullRSTY;
import com.kit.lightserver.types.response.FormOperationRSTY;

final class AdoPrimiveConverter {

    static private final Logger LOGGER = LoggerFactory.getLogger(AdoPrimiveConverter.class);

    static public AdoConverterResult<Primitive> convert(final ClientResponseRSTY responseRSTY) {

        final ConverterResult converterResult;
        if (responseRSTY instanceof ChannelNotificationEndConversationRSTY) {
            final ChannelNotification channelNotification = new ChannelNotification();
            channelNotification.type = ChannelNotification.END_CHANNEL;
            final boolean success = true;
            converterResult = new ConverterResult(success, channelNotification);
        }
        else if (responseRSTY instanceof AuthenticationResponseSuccessRSTY) {
            final AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.type = AuthenticationResponse.INF_SUCCESS;
            final boolean success = true;
            converterResult = new ConverterResult(success, authenticationResponse);

        }
        else if (responseRSTY instanceof AuthenticationResponseRSTY) {

            AuthenticationResponseRSTY authenticationResponseFailed = (AuthenticationResponseRSTY)responseRSTY;

            final byte primitiveType;
            if( authenticationResponseFailed.getType() == AuthenticationResponseRSTY.Type.FAILED_INCORRECT_PASSWORD ) {
                primitiveType = AuthenticationResponse.INF_INCORRECT_PASSWORD;
            }
            else
            if( authenticationResponseFailed.getType() == AuthenticationResponseRSTY.Type.FAILED_INEXISTENT_CLIENTID ) {
                primitiveType = AuthenticationResponse.INF_INEXISTENT_USER;
            }
            else
            if( authenticationResponseFailed.getType() == AuthenticationResponseRSTY.Type.FAILED_ALREADY_CONNECTED_OTHER_DEVICE ) {
                primitiveType = AuthenticationResponse.INF_SIMULTAENOUS_USER;
            }
            else
            if( authenticationResponseFailed.getType() == AuthenticationResponseRSTY.Type.FAILED_DATABASE_ERROR ) {
                primitiveType = AuthenticationResponse.INF_DATABASE_ERROR;
            }
            else {
                primitiveType = AuthenticationResponse.INF_FAILED;
            }

            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.type = primitiveType;
            converterResult = new ConverterResult(true, authenticationResponse);

        }
        else if (responseRSTY instanceof ChannelNotificationServerErrorRSTY) {
            final ChannelNotification channelNotification = new ChannelNotification();
            channelNotification.type = ChannelNotification.ERROR_SERVER;
            final boolean success = true;
            converterResult = new ConverterResult(success, channelNotification);
        }
        else if (responseRSTY instanceof ChannelProgressRSTY) {
            final ChannelProgressRSTY channelProgressRSTY = (ChannelProgressRSTY)responseRSTY;
            final ChannelProgress channelProgress = new ChannelProgress();
            channelProgress.numberOfSteps = (short) channelProgressRSTY.getNumberOfSteps();
            final boolean success = true;
            converterResult = new ConverterResult(success, channelProgress);
        }
        else if (responseRSTY instanceof FormOperationRSTY) {
            final FormOperationRSTY casted = (FormOperationRSTY) responseRSTY;
            converterResult = AdoFormOperationConverter.converter(casted);
        }
        else if (responseRSTY instanceof FormContentFullRSTY) {
            final FormContentFullRSTY formContentFullRSTY = (FormContentFullRSTY) responseRSTY;
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

            LOGGER.error("Unable to convert primitive. Error will be sent to client. primitiveSTY=" + responseRSTY);
            final boolean success = false;
            final ChannelNotification channelNotification = new ChannelNotification();
            channelNotification.type = ChannelNotification.ERROR_SERVER;
            final AdoConverterResult<Primitive> result = new AdoConverterResult<Primitive>(success, channelNotification);

            return result;

        }


    }


}// class
