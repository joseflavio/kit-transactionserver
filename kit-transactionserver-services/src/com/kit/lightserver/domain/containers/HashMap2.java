package com.kit.lightserver.domain.containers;

import java.util.HashMap;

public final class HashMap2<K, V> implements Map2<K, V> {

    private final HashMap<String, V> hashMap = new HashMap<>();

    private final Map2KeyGenerator<K> internalKeyGenerator;

    public HashMap2(final Map2KeyGenerator<K> internalKeyGenerator) {
        this.internalKeyGenerator = internalKeyGenerator;
    }

    @Override
    public V getByKey(final K key) {
        String stringKey = internalKeyGenerator.generateStringKey(key);
        return hashMap.get(stringKey);
    }

    @Override
    public V put(final K key, final V value) {
        String stringKey = internalKeyGenerator.generateStringKey(key);
        return hashMap.put(stringKey, value);
    }

    @Override
    public int size() {
        return hashMap.size();
    }

}// class
