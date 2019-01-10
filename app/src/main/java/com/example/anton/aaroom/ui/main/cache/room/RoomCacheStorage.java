package com.example.anton.aaroom.ui.main.cache.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.anton.aaroom.ui.main.cache.CacheStorage;

/**
 * Room cache storage
 */
@Database(entities = {CacheEntry.class}, version = 1, exportSchema = false)
public abstract class RoomCacheStorage extends RoomDatabase implements CacheStorage {
    @Nullable
    private static CacheStorage INSTANCE;

    private String name;

    public abstract CacheDao cacheDao();

    /**
     * Returns the singleton instance of the storage, creating it if it is null
     */
    public static CacheStorage instance(Context context, String name) {
        if (INSTANCE == null) {
            synchronized (RoomCacheStorage.class) {
                if (INSTANCE == null) {
                    INSTANCE = newInstance(context, name);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Returns the previously created singleton instance of the storage or null if
     * {@link #instance(Context, String)} hasn't been called before
     */
    public static CacheStorage instance() {
        return INSTANCE;
    }

    /**
     * Destroys the singleton instance of the storage
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * Creates the new instance of the cache storage.
     * Normally, this method shouldn't be called more than 1 time with the same parameters.
     * Else store the instance somewhere or use the singleton version
     * {@link #instance(Context, String)}
     */
    @NonNull
    public static CacheStorage newInstance(Context context, String name) {
        RoomCacheStorage storage = Room.databaseBuilder(context.getApplicationContext(),
                RoomCacheStorage.class, name)
                // allow queries on the main thread.
                // TODO: Don't do this on a real app! See PersistenceBasicSample for an example.
                .allowMainThreadQueries()
                .build();
        storage.name = name;
        return storage;
    }

    @Override
    public void clear() {
        cacheDao().deleteAll();
    }

    @Override
    public void destroy(Context context) {
        context.deleteDatabase(name);
    }

    @Override
    @Nullable
    public <T> T select(@NonNull Object owner, @NonNull Object key) {
        String _owner = owner.getClass().getCanonicalName();
        CacheEntry entry = cacheDao().select(_owner == null ? "" : _owner, key.toString());
        return entry == null ? null : entry.getValue();
    }

    @Override
    public <T> void upsert(@NonNull Object owner, @NonNull Object key, @NonNull T value) {
        CacheEntry entry = new CacheEntry(owner, key, value);
        if (cacheDao().insert(entry) == -1) {
            cacheDao().update(entry);
        }
    }
}
