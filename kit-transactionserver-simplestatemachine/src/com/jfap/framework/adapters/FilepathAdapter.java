package com.jfap.framework.adapters;

import com.jfap.framework.types.FilepathVO;
import com.jfap.framework.types.FilepathVO.FilepathType;

public final class FilepathAdapter implements TypeAdapter<FilepathVO, String> {

    private final FilepathType filepathType;

    public FilepathAdapter(final FilepathVO.FilepathType filepathType) {
        this.filepathType = filepathType;
    }

    @Override
    public TypeAdapterResult<FilepathVO> adapt(final String value) {

        if( value == null ) {
            return new TypeAdapterResult<FilepathVO>();
        }

        final FilepathVO filepathVO = new FilepathVO(filepathType, value);

        return new TypeAdapterResult<FilepathVO>(filepathVO);

    }

}// class
