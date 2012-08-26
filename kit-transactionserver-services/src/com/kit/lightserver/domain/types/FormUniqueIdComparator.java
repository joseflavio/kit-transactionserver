package com.kit.lightserver.domain.types;



public final class FormUniqueIdComparator {

    static private final FormUniqueIdSTYStringKeyGen keygen = new FormUniqueIdSTYStringKeyGen();

    static public int compare(final FormUniqueIdSTY o1, final FormUniqueIdSTY o2) {
        String o1key = keygen.generateStringKey(o1);
        String o2key = keygen.generateStringKey(o2);
        return o1key.compareTo(o2key);
    }

}// class
