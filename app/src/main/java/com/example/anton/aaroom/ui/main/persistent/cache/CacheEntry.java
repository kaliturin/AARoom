package com.example.anton.aaroom.ui.main.persistent.cache;

import android.support.annotation.Nullable;

/**
 * The cache entry of a cache storage
 */
public abstract class CacheEntry {

    /**
     * Returns the cache entry owner
     */
    public abstract String getOwner();

    /**
     * Returns the cache entry key
     */
    public abstract String getKey();

    /**
     * Returns creation time of the entry
     */
    public abstract long getTime();

    /**
     * Returns the serialized value of the entry
     */
    public abstract String getValue();

    /**
     * Returns the serialized type of the entry's value
     */
    public abstract String getType();

    /**
     * Returns the cache entry tag
     */
    public abstract String getTag();

    /**
     * Returns true if lifetime of the entry is expired
     */
    public boolean isExpired(long lifetime) {
        long time = getTime();
        long current = System.currentTimeMillis();
        // check the time skew
        return current + lifetime < time || time < current - lifetime;
    }

    /**
     * Returns the deserialized value of the entry
     */
    @Nullable
    public <T> T deserializeValue(CacheSerializer serializer) {
        Class<T> clazz = serializer.deserializeType(getType());
        return clazz == null ? null : serializer.deserializeValue(getValue(), clazz);
    }
}
