package com.kit.lightserver.adapters.adapterout;

import kit.primitives.authentication.AuthenticationResponse;
import kit.primitives.base.Primitive;
import kit.primitives.channel.ChannelNotification;
import kit.primitives.channel.ChannelProgress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kit.lightserver.types.response.AuthenticationResponseFailedDatabaseErrorRSTY;
import com.kit.lightserver.types.response.AuthenticationResponseFailedUserAlreadyLoggedRSTY;
import com.kit.lightserver.types.response.AuthenticationResponseFailedWrongPassowordRSTY;
import com.kit.lightserver.types.response.AuthenticationResponseSuccessRSTY;
import com.kit.lightserver.types.response.ChannelNotificationEndConversationRSTY;
import com.kit.lightserver.types.response.ChannelNotificationServerErrorRSTY;
import com.kit.lightserver.types.response.ChannelProgressRSTY;
import com.kit.lightserver.types.response.ClientResponseRSTY;
import com.kit.lightserver.types.response.FormContentFullRSTY;
import com.kit.lightserver.types.response.FormOperationRSTY;

final class AdoPrimiveConverter {

    static private final Logger LOGGER = LoggerFactory.getLogger(AdoPrimiveConverter.class);

    static public AdoConverterResult<Primitive> convert(final ClientResponseRSTY clientResponseRSTY) {

        final ConverterResult converterResult;
        if (clientResponseRSTY instanceof ChannelNotificationEndConversationRSTY) {
            final ChannelNotification channelNotification = new ChannelNotification();
            channelNotification.type = ChannelNotification.END_CHANNEL;
            final boolean success = true;
            converterResult = new ConverterResult(success, channelNotification);
        }
        else if (clientResponseRSTY instanceof AuthenticationResponseSuccessRSTY) {
            final AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.type = AuthenticationResponse.SUCCESS;
            final boolean success = true;
            converterResult = new ConverterResult(success, authenticationResponse);

        }
        else if (clientResponseRSTY instanceof AuthenticationResponseFailedWrongPassowordRSTY) {
            final AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.type = AuthenticationResponse.FAILED;
            final boolean success = true;
            converterResult = new ConverterResult(success, authenticationResponse);
        }
        else if (clientResponseRSTY instanceof AuthenticationResponseFailedUserAlreadyLoggedRSTY) {
            final AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.type = AuthenticationResponse.CLIENTALREADYLOGGED;
            final boolean success = true;
            converterResult = new ConverterResult(success, authenticationResponse);
        }
        else if (clientResponseRSTY instanceof AuthenticationResponseFailedDatabaseErrorRSTY) {
            final AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.type = AuthenticationResponse.DATABASEERROR;
            final boolean success = true;
            converterResult = new ConverterResult(success, authenticationResponse);
        }
        else if (clientResponseRSTY instanceof ChannelNotificationServerErrorRSTY) {
            final ChannelNotification channelNotification = new ChannelNotification();
            channelNotification.type = ChannelNotification.ERROR_SERVER;
            final boolean success = true;
            converterResult = new ConverterResult(success, channelNotification);
        }
        else if (clientResponseRSTY instanceof ChannelProgressRSTY) {
            final ChannelProgressRSTY channelProgressRSTY = (ChannelProgressRSTY)clientResponseRSTY;
            final ChannelProgress channelProgress = new ChannelProgress();
            channelProgress.numberOfSteps = (short) channelProgressRSTY.getNumberOfSteps();
            final boolean success = true;
            converterResult = new ConverterResult(success, channelProgress);
        }
        else if (clientResponseRSTY instanceof FormOperationRSTY) {
            final FormOperationRSTY casted = (FormOperationRSTY) clientResponseRSTY;
            converterResult = AdoFormOperationConverter.converter(casted);
        }
        else if (clientResponseRSTY instanceof FormContentFullRSTY) {
            final FormContentFullRSTY formContentFullRSTY = (FormContentFullRSTY) clientResponseRSTY;
            converterResult = FormContentFullConverter.convertForm(formContentFullRSTY);
        }
        else {
            converterResult = new ConverterResult();
        }

        if( converterResult.isSuccess() ) {

            final boolean success = true;
            final Primitive primitiveToSend = converterResult.getPrimitiveToSend();
            final AdoConverterResult<Primitive> result = new AdoConverterResult<Primitive>(success, primitiveToSend);

            return result;

        } else {

            LOGGER.error("Unable to convert primitive. Error will be sent to client. primitiveSTY=" + clientResponseRSTY);
            final boolean success = false;
            final ChannelNotification channelNotification = new ChannelNotification();
            channelNotification.type = ChannelNotification.ERROR_SERVER;
            final AdoConverterResult<Primitive> result = new AdoConverterResult<Primitive>(success, channelNotification);

            return result;

        }


    }


}// class
