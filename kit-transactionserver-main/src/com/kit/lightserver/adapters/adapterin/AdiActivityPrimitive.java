package com.kit.lightserver.adapters.adapterin;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import kit.primitives.activity.ActivityPrimitive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kit.lightserver.domain.types.CoordenadaGpsSTY;
import com.kit.lightserver.statemachine.events.ActivityGpsSME;
import com.kit.lightserver.statemachine.states.KitEventSME;

final class AdiActivityPrimitive {

    static private final Logger LOGGER = LoggerFactory.getLogger(AdiActivityPrimitive.class);

    public static ReceivedPrimitiveConverterResult<KitEventSME> adapt(final ActivityPrimitive castedPrimitive) {

        LOGGER.info("castedPrimitive.type="+castedPrimitive.type);

        final boolean moreActivitiesAvailable;
        if( castedPrimitive.type == ActivityPrimitive.RES_LOG_MORE ) {
            moreActivitiesAvailable = true;
        }
        else if( castedPrimitive.type == ActivityPrimitive.RES_LOG_EMPTY ) {
            moreActivitiesAvailable = false;
        }
        else {
            LOGGER.error("Unexpected activity type. type={}", Integer.valueOf(castedPrimitive.type) );
            return new ReceivedPrimitiveConverterResult<KitEventSME>();
        }

        List<CoordenadaGpsSTY> coordenadas = new LinkedList<>();
        if( castedPrimitive.content != null ) {

            try( DataInputStream dis = new DataInputStream(new ByteArrayInputStream(castedPrimitive.content)) ) {

                while( dis.available() > 0 ) {
                    long logicalClock = dis.readLong();
                    long time =  dis.readLong();
                    byte activityPosition =  dis.readByte();
                    float latitude = dis.readFloat();
                    float longitude = dis.readFloat();
                    float accuracy = dis.readFloat();
                    //LOGGER.info("logicalClock="+logicalClock);
                    //LOGGER.info("time="+new Date(time) );
                    //LOGGER.info("activityPosition="+activityPosition);
                    //LOGGER.info("latitude="+latitude);
                    //LOGGER.info("longitude="+longitude);
                    //LOGGER.info("accuracy="+accuracy);
                    //LOGGER.info("dis.available()="+dis.available());
                    CoordenadaGpsSTY coordenadaGps = new CoordenadaGpsSTY(logicalClock, time, activityPosition, latitude, longitude, accuracy);
                    LOGGER.info("coordenadaGps="+coordenadaGps);
                    coordenadas.add(coordenadaGps);
                    LOGGER.info("----------");
                }

            }
            catch (IOException e) {
                LOGGER.error("Unexpected error.", e);
                return new ReceivedPrimitiveConverterResult<KitEventSME>();
            }

        }

        ActivityGpsSME converted = new ActivityGpsSME(moreActivitiesAvailable, coordenadas);

        ReceivedPrimitiveConverterResult<KitEventSME> result = new ReceivedPrimitiveConverterResult<KitEventSME>(true, converted);
        return result;

    }

}
