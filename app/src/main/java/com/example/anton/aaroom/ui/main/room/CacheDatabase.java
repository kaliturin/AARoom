package com.example.anton.aaroom.ui.main.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * OrmLite supported cache database
 */
@Database(entities = {CacheEntry.class}, version = 1, exportSchema = false)
public abstract class CacheDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "model-cache.db";

    private static CacheDatabase INSTANCE;

    public abstract CacheDao cacheDao();

    public static CacheDatabase instance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    CacheDatabase.class, DATABASE_NAME)
                    // TODO: allow queries on the main thread.
                    // Don't do this on a real app! See PersistenceBasicSample for an example.
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    public static CacheDatabase instance() {
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public static void init(Context context) {
        instance(context);
    }

    @Nullable
    public <T> T select(@NonNull Object owner, @NonNull Object key) {
        String _owner = owner.getClass().getCanonicalName();
        CacheEntry entry = cacheDao().select(_owner == null ? "" : _owner, key.toString());
        return entry == null ? null : entry.getValue();
    }

//    public <T> long insert(@NonNull Object owner, @NonNull Object key, @NonNull T value) {
//        CacheEntry entry = new CacheEntry(owner, key, value);
//        return cacheDao().insert(entry);
//    }

    public <T> void upsert(@NonNull Object owner, @NonNull Object key, @NonNull T value) {
        CacheEntry entry = new CacheEntry(owner, key, value);
        upsert(entry);
    }

    public void upsert(CacheEntry entity) {
        if (cacheDao().insert(entity) == -1) {
            cacheDao().update(entity);
        }
    }

    public void clear() {
        cacheDao().deleteAll();
    }

    public void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }
}
