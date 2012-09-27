package com.kit.lightserver.domain.types;

import java.util.Date;

public class CoordenadaGpsSTY {

    private final long logicalClock;
    private final Date time;
    private final byte activityPosition;
    private final float latitude;
    private final float longitude;
    private final float accuracy;

    public CoordenadaGpsSTY(final long logicalClock, final long time, final byte activityPosition, final float latitude, final float longitude, final float accuracy) {
        this.logicalClock = logicalClock;
        this.time = new Date(time);
        this.activityPosition = activityPosition;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
    }

    @Override
    public String toString() {
        return "CoordenadaGpsSTY [logicalClock=" + logicalClock + ", time=" + time + ", activityPosition=" + activityPosition + ", latitude=" + latitude
                + ", longitude=" + longitude + ", accuracy=" + accuracy + "]";
    }



}
