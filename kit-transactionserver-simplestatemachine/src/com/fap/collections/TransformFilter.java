package com.fap.collections;

public interface TransformFilter<R, T> {

    R transform(T t);

}
