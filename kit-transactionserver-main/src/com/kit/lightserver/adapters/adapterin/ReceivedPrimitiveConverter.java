package com.kit.lightserver.adapters.adapterin;




import kit.primitives.activity.ActivityPrimitive;
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
        try {
            return convert2(receivedPrimitive);
        } catch (Exception e) {
            ReceivedPrimitiveConverterResult<KitEventSME> error = new ReceivedPrimitiveConverterResult<KitEventSME>();
            return error;
        }
    }

    static private ReceivedPrimitiveConverterResult<KitEventSME> convert2(final Primitive receivedPrimitive) throws Exception {

        final ReceivedPrimitiveConverterResult<KitEventSME> result;
        if( receivedPrimitive instanceof AuthenticationRequest ) {
            final AuthenticationRequest castedPrimitive = (AuthenticationRequest)receivedPrimitive;
            final AuthenticationRequestSME convertedPrimitiveSTY = AdiAuthenticationRequest.adapt(castedPrimitive);
            result = new ReceivedPrimitiveConverterResult<KitEventSME>(true, convertedPrimitiveSTY);
        }
        else if( receivedPrimitive instanceof ChannelNotification ) {
            final KitEventSME event = AdiChannelNotification.adapt((ChannelNotification)receivedPrimitive);
            result = new ReceivedPrimitiveConverterResult<KitEventSME>(true, event);
        }
        else if( receivedPrimitive instanceof FormOperation ) {
            result = AdiFormOperation.adapt( (FormOperation)receivedPrimitive );
        }
        else if( receivedPrimitive instanceof FormContent ) {
            result = AdiFormContent.adapt( (FormContent)receivedPrimitive );
        }
        else if( receivedPrimitive instanceof ActivityPrimitive ) {
            final ActivityPrimitive castedPrimitive = (ActivityPrimitive)receivedPrimitive;
            result = AdiActivityPrimitive.adapt(castedPrimitive);
        }
        else {
            result = new ReceivedPrimitiveConverterResult<KitEventSME>();
        }
        return result;
    }

}// class
