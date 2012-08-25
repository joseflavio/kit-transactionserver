package com.kit.lightserver.domain.containers;

import java.util.HashMap;

public final class HashMap2<K, V> implements Map2<K, V> {

    private final HashMap<K, V> hashMap = new HashMap<>();

    @Override
    public V getByKey(final K key) {
        return hashMap.get(key);
    }

    @Override
    public V put(final K key, final V value) {
        return hashMap.put(key, value);
    }

    @Override
    public int size() {
        return hashMap.size();
    }

}// class
