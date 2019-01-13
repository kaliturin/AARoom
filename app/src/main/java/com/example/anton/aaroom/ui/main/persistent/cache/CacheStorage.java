package com.example.anton.aaroom.ui.main.persistent.cache;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Persistent cache storage methods
 */
public interface CacheStorage {

    /**
     * Returns the cache entry fields serializer
     */
    CacheSerializer getSerializer();

    /**
     * Sets the cache entry fields serializer
     */
    void setSerializer(CacheSerializer serializer);

    /**
     * Returns the type of the cache storage
     */
    int getType();

    /**
     * Deletes the all cache entries from the storage (may take long time for a big storage)
     */
    void deleteAll();

    /**
     * Deletes the all cache entries by owner and tag
     *
     * @param owner the owner of the cache entry - usually "this" object or just a string
     * @param tag   the additional id of the cache entry
     */
    void deleteByOwnerAndTag(Object owner, String tag);

    /**
     * Deletes the all cache entries of the owner with defined tag and keys not in the passed list
     *
     * @param owner the owner of the cache entry - usually "this" object or just a string
     * @param tag   the additional id of the cache entry
     * @param keys  the list of the keys
     */
    void deleteByOwnerAndTagAndKeyNotIn(Object owner, String tag, List<Object> keys);

    /**
     * Deletes the all cache entries by owner
     *
     * @param owner the owner of the cache entry - usually "this" object or just a string
     */
    void deleteByOwner(Object owner);

    /**
     * Deletes the all cache entries by tag
     *
     * @param tag the additional id of the cache entry
     */
    void deleteByTag(String tag);

    /**
     * Deletes the entry from the storage
     */
    void delete(CacheEntry entry);

    /**
     * Destroys the cache storage (removing it's files if there are some)
     */
    void destroy(Context context);

    /**
     * Closes the cache storage
     */
    void close();

    /**
     * Inserts or updates the cache entry
     */
    void setEntry(@NonNull CacheEntry entry);

    /**
     * Finds the cache entry by its owner and key
     *
     * @param owner the owner of the cache entry - usually "this" object or just a string
     * @param key   the unique id of the cache entry - must have toString overridden in way of
     *              returning the unique string
     */
    @Nullable
    CacheEntry getEntry(Object owner, Object key);

    /**
     * Inserts or updates the cache entry.
     *
     * @param owner the owner of the cache entry - usually "this" object or just a string
     * @param key   the unique id of the cache entry - must have toString overridden in way of
     *              returning the unique string
     * @param value the actual cache value
     * @param time  the actual creation time of the cache value
     * @param tag   the additional id of the cache entry
     */
    <T> void setValue(Object owner, Object key, T value, long time, String tag);

    /**
     * Inserts or updates the cache entry
     *
     * @param owner the owner of the cache entry - usually "this" object or just a string
     * @param key   the unique id of the cache entry - must have toString overridden in way of
     *              returning the unique string
     * @param value the actual cache value
     * @param tag   the additional id of the cache entry
     */
    default <T> void setValue(Object owner, Object key, T value, String tag) {
        setValue(owner, key, value, System.currentTimeMillis(), tag);
    }

    /**
     * Inserts or updates the cache entry
     *
     * @param owner the owner of the cache entry - usually "this" object or just a string
     * @param key   the unique id of the cache entry - must have toString overridden in way of
     *              returning the unique string
     * @param value the actual cache value
     */
    default <T> void setValue(Object owner, Object key, T value) {
        setValue(owner, key, value, System.currentTimeMillis(), null);
    }

    /**
     * Returns the deserialized value of the cache entry found by its owner and key
     *
     * @param owner the owner of the cache entry - usually "this" object or just a string
     * @param key   the unique id of the cache entry - must have toString overridden in way of
     *              returning the unique string
     */
    @Nullable
    default <T> T getValue(Object owner, Object key) {
        CacheEntry entry = getEntry(owner, key);
        return getValue(entry);
    }

    /**
     * Returns the deserialized value of the cache entry
     */
    @Nullable
    default <T> T getValue(CacheEntry entry) {
        return entry == null ? null : entry.deserializeValue(getSerializer());
    }
}
