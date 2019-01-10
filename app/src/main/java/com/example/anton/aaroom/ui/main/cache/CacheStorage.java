package com.example.anton.aaroom.ui.main.cache;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Persistent cache storage methods
 */
public interface CacheStorage {

    /**
     * Clears the cache storage
     */
    void clear();

    /**
     * Destroys the cache storage (removing the files if there are some)
     */
    void destroy(Context context);

    /**
     * Selects the cache value by its owner and key
     */
    @Nullable
    <T> T select(@NonNull Object owner, @NonNull Object key);

    /**
     * Inserts or updates the cache entry
     */
    <T> void upsert(@NonNull Object owner, @NonNull Object key, @NonNull T value);
}
