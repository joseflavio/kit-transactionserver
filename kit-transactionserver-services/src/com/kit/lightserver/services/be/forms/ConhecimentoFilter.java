package com.kit.lightserver.services.be.forms;

import com.jfap.util.collections.SmartCollectionTransformFilter;
import com.kit.lightserver.domain.ConhecimentoSTY;
import com.kit.lightserver.domain.FormSTY;

final class ConhecimentoFilter implements SmartCollectionTransformFilter<ConhecimentoSTY, FormSTY> {

    @Override
    public ConhecimentoSTY transform(final FormSTY formSTY) {
        if( formSTY instanceof ConhecimentoSTY ) {
            final ConhecimentoSTY conhecimentoSTY = (ConhecimentoSTY) formSTY;
            return conhecimentoSTY;
        }
        return null;
    }

}// class
