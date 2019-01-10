package com.example.anton.aaroom.ui.main.cache.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.anton.aaroom.ui.main.cache.CacheStorage;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * OrmLite cache storage
 */
public class OrmLiteCacheStorage extends OrmLiteSqliteOpenHelper implements CacheStorage {
    @Nullable
    private static CacheStorage INSTANCE;

    private Dao<CacheEntry, String> cacheDao;

    private OrmLiteCacheStorage(Context context, String name) {
        super(context, name, null, CacheEntry.VERSION);
    }

    /**
     * Returns the singleton instance of the storage, creating it if it is null
     */
    public static CacheStorage instance(Context context, String name) {
        if (INSTANCE == null) {
            synchronized (OrmLiteCacheStorage.class) {
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
        return new OrmLiteCacheStorage(context.getApplicationContext(), name);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, CacheEntry.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, CacheEntry.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Dao<CacheEntry, String> cacheDao() throws SQLException {
        if (cacheDao == null) {
            cacheDao = getDao(CacheEntry.class);
        }
        return cacheDao;
    }

    @Override
    public void close() {
        cacheDao = null;
        super.close();
    }

    @Override
    public void clear() {
        try {
            TableUtils.clearTable(getConnectionSource(), CacheEntry.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy(Context context) {
        close();
        context.deleteDatabase(getDatabaseName());
    }

    @Override
    @Nullable
    public <T> T select(@NonNull Object owner, @NonNull Object key) {
        CacheEntry entry;
        try {
            entry = cacheDao().queryForId(CacheEntry.buildId(owner, key));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entry == null ? null : entry.getValue();
    }

    @Override
    public <T> void upsert(@NonNull Object owner, @NonNull Object key, @NonNull T value) {
        CacheEntry entry = new CacheEntry(owner, key, value);
        try {
            cacheDao().createOrUpdate(entry);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}