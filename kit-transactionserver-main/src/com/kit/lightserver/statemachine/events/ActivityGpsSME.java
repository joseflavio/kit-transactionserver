package com.kit.lightserver.statemachine.events;

import java.util.List;

import com.kit.lightserver.domain.types.CoordenadaGpsSTY;
import com.kit.lightserver.statemachine.states.KitEventSME;

public final class ActivityGpsSME implements KitEventSME {

    private final boolean moreActivitiesAvailable;
    private final List<CoordenadaGpsSTY> coordenadas;

    public ActivityGpsSME(final boolean moreActivitiesAvailable, final List<CoordenadaGpsSTY> coordenadas) {
        this.moreActivitiesAvailable = moreActivitiesAvailable;
        this.coordenadas = coordenadas;
    }

    public boolean isMoreActivitiesAvailable() {
        return moreActivitiesAvailable;
    }

    public List<CoordenadaGpsSTY> getCoordenadas() {
        return coordenadas;
    }

    @Override
    public String toString() {
        final int maxLen = 10;
        return "ActivityGpsSME [moreActivitiesAvailable=" + moreActivitiesAvailable + ", coordenadas="
                + (coordenadas != null ? coordenadas.subList(0, Math.min(coordenadas.size(), maxLen)) : null) + "]";
    }

}
