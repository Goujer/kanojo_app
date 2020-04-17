package com.google.tagmanager;

import android.annotation.TargetApi;
import android.util.LruCache;
import com.google.tagmanager.CacheFactory;

@TargetApi(12)
class LRUCache<K, V> implements Cache<K, V> {
    private LruCache<K, V> lruCache;

    LRUCache(int maxSize, final CacheFactory.CacheSizeManager<K, V> sizeManager) {
        this.lruCache = new LruCache<K, V>(maxSize) {
            /* access modifiers changed from: protected */
            public int sizeOf(K key, V value) {
                return sizeManager.sizeOf(key, value);
            }
        };
    }

    public void put(K key, V value) {
        this.lruCache.put(key, value);
    }

    public V get(K key) {
        return this.lruCache.get(key);
    }
}
