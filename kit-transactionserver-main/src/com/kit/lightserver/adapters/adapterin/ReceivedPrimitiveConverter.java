package com.kit.lightserver.adapters.adapterin;




import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Date;

import kit.primitives.activity.ActivityPrimitive;
import kit.primitives.authentication.AuthenticationRequest;
import kit.primitives.base.Primitive;
import kit.primitives.channel.ChannelNotification;
import kit.primitives.forms.FormContent;
import kit.primitives.forms.FormOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kit.lightserver.domain.types.CoordenadaGpsSTY;
import com.kit.lightserver.statemachine.events.ActivityGpsSME;
import com.kit.lightserver.statemachine.events.AuthenticationRequestSME;
import com.kit.lightserver.statemachine.states.KitEventSME;

final class ReceivedPrimitiveConverter {

    static private final Logger LOGGER = LoggerFactory.getLogger(ReceivedPrimitiveConverter.class);

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
            LOGGER.info("castedPrimitive.type="+castedPrimitive.type);


            if( castedPrimitive.content != null ) {
                LOGGER.info("castedPrimitive.content.length="+castedPrimitive.content.length);
                DataInputStream dis = new DataInputStream(new ByteArrayInputStream(castedPrimitive.content));

                while( dis.available() > 0 ) {
                    long logicalClock = dis.readLong();
                    long time =  dis.readLong();
                    byte activityPosition =  dis.readByte();
                    float latitude = dis.readFloat();
                    float longitude = dis.readFloat();
                    float accuracy = dis.readFloat();

                    LOGGER.info("logicalClock="+logicalClock);
                    LOGGER.info("time="+new Date(time) );
                    LOGGER.info("activityPosition="+activityPosition);
                    LOGGER.info("latitude="+latitude);
                    LOGGER.info("longitude="+longitude);
                    LOGGER.info("accuracy="+accuracy);
                    LOGGER.info("dis.available()="+dis.available());

                    CoordenadaGpsSTY coordenadaGps = new CoordenadaGpsSTY(logicalClock, time, activityPosition, latitude, longitude, accuracy);
                    LOGGER.info("coordenadaGps="+coordenadaGps);

                    LOGGER.info("----------");
                }

            }

            ActivityGpsSME converted = new ActivityGpsSME();
            result = new ReceivedPrimitiveConverterResult<KitEventSME>(true, converted);
        }
        else {
            result = new ReceivedPrimitiveConverterResult<KitEventSME>();
        }
        return result;
    }

    static long getLong(final byte[] b, final int off) {
        return ((b[off + 7] & 0xFFL) << 0) +
               ((b[off + 6] & 0xFFL) << 8) +
               ((b[off + 5] & 0xFFL) << 16) +
               ((b[off + 4] & 0xFFL) << 24) +
               ((b[off + 3] & 0xFFL) << 32) +
               ((b[off + 2] & 0xFFL) << 40) +
               ((b[off + 1] & 0xFFL) << 48) +
               (((long) b[off + 0]) << 56);
    }

}// class
