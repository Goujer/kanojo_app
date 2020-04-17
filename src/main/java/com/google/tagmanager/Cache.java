package com.google.tagmanager;

interface Cache<K, V> {
    V get(K k);

    void put(K k, V v);
}
