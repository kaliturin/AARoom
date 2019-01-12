package com.example.anton.aaroom.ui.main.cache;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.anton.aaroom.App_;
import com.example.anton.aaroom.Config;
import com.example.anton.aaroom.ui.main.cache.ormlite.OrmLiteCacheStorage;
import com.example.anton.aaroom.ui.main.cache.room.RoomCacheStorage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Persistent cache storage manager
 */
public class CacheStorageManager {

    // The storage ORM types
    public static final int ORM_LITE = OrmLiteCacheStorage.TYPE;
    public static final int ROOM = RoomCacheStorage.TYPE;

    private static final Map<String, CacheStorage> map = new ConcurrentHashMap<>();

    /**
     * Returns the singleton instance of the storage with the default type, creating it if it doesn't exist
     */
    public static CacheStorage instance(String name) {
        return instance(Config.CACHE_MODELS_TYPE, name);
    }

    /**
     * Returns the singleton instance of the storage with the default type, creating it if it doesn't exist
     */
    public static CacheStorage instance(int type, String name) {
        return instance(App_.getInstance(), type, name);
    }

    /**
     * Returns the singleton instance of the storage with the defined type, creating it if it doesn't exist
     */
    @NonNull
    public static CacheStorage instance(Context context, int type, String name) {
        // find storage in the map and create it if not found
        CacheStorage cache = map.get(name);
        if (cache == null) {
            synchronized (CacheStorageManager.class) {
                cache = map.get(name);
                if (cache == null) {
                    cache = newInstance(context, type, name);
                    map.put(name, cache);
                    return cache;
                }
            }
        }

        // check the type of the found storage
        if (cache.getType() != type) {
            throw new IllegalArgumentException(String.format("Persistent cache storage with " +
                    "name = %s has been already created with the different type = %d", name, type));
        }

        return cache;
    }

    /**
     * Closes the storage and removes it's singleton instance from the manager's map
     */
    public static void close(String name) {
        synchronized (CacheStorageManager.class) {
            CacheStorage cache = map.remove(name);
            if (cache != null) {
                cache.close();
            }
        }
    }

    /**
     * Destroys the storage (removing it's files if there are some)
     */
    public static void destroy(String name) {
        synchronized (CacheStorageManager.class) {
            CacheStorage cache = map.remove(name);
            if (cache != null) {
                cache.destroy(App_.getInstance());
            }
        }
    }

    /**
     * Returns the new created instance of the cache storage.
     * Normally, this method shouldn't be called more than 1 time with the same parameters.
     */
    @NonNull
    private static CacheStorage newInstance(Context context, int type, String name) {
        switch (type) {
            case ORM_LITE:
                return OrmLiteCacheStorage.newInstance(context, name);
            case ROOM:
                return RoomCacheStorage.newInstance(context, name);
            default:
                throw new IllegalArgumentException(String.format("Unknown persistent " +
                        "cache storage with name = %s, type = %d", name, type));
        }
    }
}
