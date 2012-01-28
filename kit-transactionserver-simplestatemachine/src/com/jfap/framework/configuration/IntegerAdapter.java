package com.jfap.framework.configuration;

public final class IntegerAdapter implements TypeAdapter<Integer> {

    @Override
    public TypeAdapterResult<Integer> adapt(final String propertyValue) {
        try {
            int intValue = Integer.parseInt(propertyValue);
            return new TypeAdapterResult<Integer>(Integer.valueOf(intValue));
        } catch (NumberFormatException e) {
            return new TypeAdapterResult<Integer>();
        }
    }

}// class
