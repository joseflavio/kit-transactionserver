package com.kit.lightserver.services.be.forms;

import com.jfap.util.collections.SmartCollectionTransformFilter;
import com.kit.lightserver.domain.FormSTY;
import com.kit.lightserver.domain.NotafiscalSTY;

final class NotafiscalFilter implements SmartCollectionTransformFilter<NotafiscalSTY, FormSTY> {

    @Override
    public NotafiscalSTY transform(final FormSTY formSTY) {
        if( formSTY instanceof NotafiscalSTY ) {
            final NotafiscalSTY conhecimentoSTY = (NotafiscalSTY) formSTY;
            return conhecimentoSTY;
        }
        return null;
    }

}// class
