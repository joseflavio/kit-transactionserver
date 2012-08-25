package com.kit.lightserver.domain.filters;

import com.fap.collections.SmartCollectionTransformFilter;
import com.fap.collections.SmartCollectionTransformResult;

import com.kit.lightserver.domain.types.ConhecimentoSTY;
import com.kit.lightserver.domain.types.FormClientRowIdSTY;
import com.kit.lightserver.domain.types.FormSTY;

public final class ConhecimentoRowIdTransformFilter implements SmartCollectionTransformFilter<FormClientRowIdSTY, FormSTY> {

    @Override
    public SmartCollectionTransformResult<FormClientRowIdSTY> transform(final FormSTY formSTY) {
        if( formSTY instanceof ConhecimentoSTY ) {
            ConhecimentoSTY conhecimento = ((ConhecimentoSTY) formSTY);
            return new SmartCollectionTransformResult<FormClientRowIdSTY>( conhecimento.getFormClientRowId() );
        }
        return new SmartCollectionTransformResult<FormClientRowIdSTY>();
    }

}// class
