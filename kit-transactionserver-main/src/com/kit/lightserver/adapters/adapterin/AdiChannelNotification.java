package com.kit.lightserver.adapters.adapterin;

import kit.primitives.channel.ChannelNotification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kit.lightserver.statemachine.events.ChannelNotificationSME;
import com.kit.lightserver.statemachine.events.ServerErrorConvertingPrimitiveSME;
import com.kit.lightserver.statemachine.states.KitEventSME;


final class AdiChannelNotification {

	static private final Logger LOGGER = LoggerFactory.getLogger(AdiChannelNotification.class);

	static public KitEventSME adapt(final ChannelNotification primitive) {

		final KitEventSME result;
		if(primitive.type == ChannelNotification.END_CHANNEL) {
			result =  new ChannelNotificationSME(ChannelNotificationSME.Type.END_CHANNEL);
		}
		else if(primitive.type == ChannelNotification.ERROR_PROTOCOL) {
			result = new ChannelNotificationSME(ChannelNotificationSME.Type.ERROR_PROTOCOL);
		}
		else {
			LOGGER.error("ChannelNotification of unknow type. type="+primitive.type);
			result = new ServerErrorConvertingPrimitiveSME();
		}

		return result;

	}

}// class
