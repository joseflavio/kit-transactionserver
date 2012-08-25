package com.fap.collections;

public interface SmartCollectionTransformFilter<R, T> {

    SmartCollectionTransformResult<R> transform(T t);

}
