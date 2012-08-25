package com.kit.lightserver.domain.containers;

public interface Map2<K, V> {

    V getByKey(K key);

    V put(K key, V value);

    int size();

}// interface
