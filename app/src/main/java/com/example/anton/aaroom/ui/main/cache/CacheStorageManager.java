package com.example.anton.aaroom.ui.main.cache;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.anton.aaroom.ui.main.cache.ormlite.OrmLiteCacheStorage;
import com.example.anton.aaroom.ui.main.cache.room.RoomCacheStorage;

/**
 * Persistent cache storage manager
 */
public class CacheStorageManager {
    public static final int ORM_LITE = 1;
    public static final int ROOM = 2;
    private static int lastType = ORM_LITE;

    /**
     * Returns the singleton instance of the storage with the defined type, creating it if it is null
     */
    @NonNull
    public static CacheStorage instance(Context context, int type, String name) {
        lastType = type;
        switch (type) {
            case ORM_LITE:
                return OrmLiteCacheStorage.instance(context, name);
            case ROOM:
                return RoomCacheStorage.instance(context, name);
            default:
                throw new IllegalArgumentException("Unknown persistent cache storage type = " + type);
        }
    }

    /**
     * Returns the previously created singleton instance of the storage with the defined type or
     * null if {@link #instance(Context, int, String)} hasn't been called before
     */
    @Nullable
    public static CacheStorage instance(int type) {
        lastType = type;
        switch (type) {
            case ORM_LITE:
                return OrmLiteCacheStorage.instance();
            case ROOM:
                return RoomCacheStorage.instance();
            default:
                throw new IllegalArgumentException("Unknown persistent cache storage type = " + type);
        }
    }

    /**
     * Returns the previously created singleton instance of the storage with the last used type or
     * null if {@link #instance(Context, int, String)} hasn't been called before
     */
    public static CacheStorage instance() {
        return instance(lastType);
    }

    /**
     * Destroys the singleton instance of the storage with the defined type
     */
    public static void destroyInstance(int type) {
        lastType = type;
        switch (type) {
            case ORM_LITE:
                OrmLiteCacheStorage.destroyInstance();
            case ROOM:
                RoomCacheStorage.destroyInstance();
            default:
                throw new IllegalArgumentException("Unknown persistent cache storage type = " + type);
        }
    }

    /**
     * Destroys the singleton instance of the storage with the last used type
     */
    public static void destroyInstance() {
        destroyInstance(lastType);
    }

    /**
     * Returns the new created instance of the cache storage.
     * Normally, this method shouldn't be called more than 1 time with the same parameters.
     * Else you probably should store the instance somewhere or use the singleton version
     * {@link #instance(Context, int, String)}
     */
    @NonNull
    public static CacheStorage newInstance(Context context, int type, String name) {
        switch (type) {
            case ORM_LITE:
                return OrmLiteCacheStorage.newInstance(context, name);
            case ROOM:
                return RoomCacheStorage.newInstance(context, name);
            default:
                throw new IllegalArgumentException("Unknown persistent cache storage type = " + type);
        }
    }
}
