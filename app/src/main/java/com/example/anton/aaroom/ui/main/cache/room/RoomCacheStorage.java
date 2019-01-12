package com.example.anton.aaroom.ui.main.cache.room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.anton.aaroom.ui.main.cache.CacheEntry;
import com.example.anton.aaroom.ui.main.cache.CacheSerializer;
import com.example.anton.aaroom.ui.main.cache.CacheStorage;

import lombok.Getter;
import lombok.Setter;
import timber.log.Timber;

/**
 * Room cache storage
 */
@Database(entities = {RoomCacheEntry.class}, version = 1, exportSchema = false)
public abstract class RoomCacheStorage extends RoomDatabase implements CacheStorage {

    // The storage type identifier
    public static final int TYPE = 2;

    // The storage name
    private String name;

    // Default cache entry fields serializer
    @Getter
    @Setter
    private CacheSerializer serializer = new CacheSerializer();

    public abstract CacheDao cacheDao();

    /**
     * Creates the new instance of the cache storage.
     * Normally, this method shouldn't be called more than 1 time with the same parameters.
     */
    @NonNull
    public static CacheStorage newInstance(Context context, String name) {
        RoomCacheStorage storage = Room.databaseBuilder(context.getApplicationContext(),
                RoomCacheStorage.class, name)
                // allow queries on the main thread.
                // Don't do this on a real app! See PersistenceBasicSample for an example.
                .allowMainThreadQueries()
                .build();
        storage.name = name;
        return storage;
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public void deleteAll() {
        cacheDao().deleteAll();
        vacuum();
    }

    @Override
    public void deleteBy(Object owner, String tag) {
        String _owner = serializer.serializeOwner(owner, "");
        cacheDao().deleteBy(_owner, tag);
        vacuum();
    }

    @Override
    public void delete(CacheEntry entry) {
        RoomCacheEntry _entry = new RoomCacheEntry(entry);
        cacheDao().delete(_entry);
    }

    @Override
    public void destroy(Context context) {
        Timber.d("Destroying persistent cache: " + name);
        close();
        context.deleteDatabase(name);
    }

    @Override
    public void setEntry(@NonNull CacheEntry entry) {
        RoomCacheEntry _entry = new RoomCacheEntry(entry);
        if (cacheDao().insert(_entry) == -1) {
            cacheDao().update(_entry);
        }
    }

    @Override
    @Nullable
    public CacheEntry getEntry(Object owner, Object key) {
        String _owner = serializer.serializeOwner(owner, "");
        String _key = serializer.serializeKey(key, "");
        return cacheDao().select(_owner, _key);
    }

    @Override
    public <T> void setValue(Object owner, Object key, T value, long time, String tag) {
        RoomCacheEntry entry = new RoomCacheEntry(serializer, owner, key, value, time, tag);
        if (cacheDao().insert(entry) == -1) {
            cacheDao().update(entry);
        }
    }

    public void vacuum() {
        final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
        if (!_db.inTransaction()) {
            _db.execSQL("VACUUM");
        }
    }

    @Override
    public void init(@NonNull DatabaseConfiguration configuration) {
        super.init(configuration);
        final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
        _db.execSQL("PRAGMA auto_vacuum = FULL;");
    }
}
