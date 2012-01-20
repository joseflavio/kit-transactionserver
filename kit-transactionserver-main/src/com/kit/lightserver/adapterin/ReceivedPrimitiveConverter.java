package com.kit.lightserver.adapterin;


import kit.primitives.authentication.AuthenticationRequest;
import kit.primitives.base.Primitive;
import kit.primitives.channel.ChannelNotification;
import kit.primitives.forms.FormContent;
import kit.primitives.forms.FormOperation;

import com.kit.lightserver.statemachine.events.AuthenticationRequestSME;
import com.kit.lightserver.statemachine.states.KitEventSME;

final class ReceivedPrimitiveConverter {

    //static private final Logger LOGGER = LoggerFactory.getLogger(ReceivedPrimitiveConverter.class);

    static public ReceivedPrimitiveConverterResult<KitEventSME> convert(final Primitive receivedPrimitive) {

        final ReceivedPrimitiveConverterResult<KitEventSME> converterResult;
        if( receivedPrimitive instanceof AuthenticationRequest ) {
            final AuthenticationRequest primitive = (AuthenticationRequest)receivedPrimitive;
            final AuthenticationRequestSME convertedPrimitiveSTY = AdiAuthenticationRequest.adapt(primitive);
            converterResult = new ReceivedPrimitiveConverterResult<KitEventSME>(true, convertedPrimitiveSTY);
        }
        else if( receivedPrimitive instanceof ChannelNotification ) {
            final KitEventSME convertedPrimitiveSTY = AdiChannelNotification.adapt((ChannelNotification)receivedPrimitive);
            converterResult = new ReceivedPrimitiveConverterResult<KitEventSME>(true, convertedPrimitiveSTY);
        }
        else if( receivedPrimitive instanceof FormOperation ) {
            converterResult = AdiFormOperation.adapt( (FormOperation)receivedPrimitive );
        }
        else if( receivedPrimitive instanceof FormContent ) {
            converterResult = AdiFormContent.adapt( (FormContent)receivedPrimitive );
        }
        else {
            converterResult = new ReceivedPrimitiveConverterResult<KitEventSME>();
        }

        return converterResult;

    }

}// class
