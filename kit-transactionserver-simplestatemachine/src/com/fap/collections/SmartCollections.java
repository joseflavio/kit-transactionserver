package com.fap.collections;

import java.util.Collection;

public final class SmartCollections {

    static public <T> void filter(final Collection<T> output, final Collection<T> input, final SmartCollectionMatchFilter<T> filter) {
        for (final T t : input) {
            if( filter.match(t) == true ) {
                output.add(t);
            }
        }
    }

    static public <R, T> void specialFilter(final Collection<R> output, final Collection<T> input, final SmartCollectionTransformFilter<R, T> filter) {
        for (final T t : input) {
            SmartCollectionTransformResult<R> result = filter.transform(t);
            if( result.shouldAdd() == true ) {
                output.add( result.getResult() );
            }
        }
    }

}// class
