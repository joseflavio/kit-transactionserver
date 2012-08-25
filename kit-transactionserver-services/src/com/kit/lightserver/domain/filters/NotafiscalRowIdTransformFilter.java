package com.kit.lightserver.domain.filters;

import com.fap.collections.SmartCollectionTransformFilter;
import com.fap.collections.SmartCollectionTransformResult;

import com.kit.lightserver.domain.types.FormClientRowIdSTY;
import com.kit.lightserver.domain.types.FormSTY;
import com.kit.lightserver.domain.types.NotafiscalSTY;

public final class NotafiscalRowIdTransformFilter implements SmartCollectionTransformFilter<FormClientRowIdSTY, FormSTY> {

    @Override
    public SmartCollectionTransformResult<FormClientRowIdSTY> transform(final FormSTY formSTY) {
        if( formSTY instanceof NotafiscalSTY ) {
            NotafiscalSTY notafiscal = ((NotafiscalSTY) formSTY);
            FormClientRowIdSTY clientRowIdSTY = notafiscal.getFormClientRowId();
            return new SmartCollectionTransformResult<FormClientRowIdSTY>( clientRowIdSTY );
        }
        return new SmartCollectionTransformResult<FormClientRowIdSTY>();
    }

}// class
