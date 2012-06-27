package com.kit.lightserver.services.be.forms;

import com.fap.collections.SmartCollectionTransformFilter;

import com.kit.lightserver.domain.types.FormNotafiscalRowIdSTY;
import com.kit.lightserver.domain.types.FormSTY;
import com.kit.lightserver.domain.types.NotafiscalSTY;

final class NotafiscalFilter implements SmartCollectionTransformFilter<FormNotafiscalRowIdSTY, FormSTY> {

    @Override
    public FormNotafiscalRowIdSTY transform(final FormSTY formSTY) {
        if( formSTY instanceof NotafiscalSTY ) {
            final NotafiscalSTY form = (NotafiscalSTY) formSTY;
            return form.getKtRowId();
        }
        return null;
    }

}// class
