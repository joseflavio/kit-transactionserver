package com.kit.lightserver.services.db.forms.flags;

import com.fap.collections.SmartCollectionTransformFilter;

import com.kit.lightserver.domain.types.ConhecimentoSTY;
import com.kit.lightserver.domain.types.FormConhecimentoRowIdSTY;
import com.kit.lightserver.domain.types.FormSTY;

final class ConhecimentoRowIdFilter implements SmartCollectionTransformFilter<FormConhecimentoRowIdSTY, FormSTY> {

    @Override
    public FormConhecimentoRowIdSTY transform(final FormSTY formSTY) {
        if( formSTY instanceof ConhecimentoSTY ) {
            final ConhecimentoSTY conhecimentoSTY = (ConhecimentoSTY) formSTY;
            return conhecimentoSTY.getKtFormRowId();
        }
        return null;
    }

}// class
