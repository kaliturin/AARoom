package com.example.anton.aaroom.ui.main.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * OrmLite supported cache database
 */
public class CacheDatabase extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "model-cache.db";
    private static final int DATABASE_VERSION = 1;

    private static CacheDatabase INSTANCE;

    private Dao<CacheEntry, String> cacheDao;

    private CacheDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static CacheDatabase instance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new CacheDatabase(context.getApplicationContext());
        }
        return INSTANCE;
    }

    public static CacheDatabase instance() {
        return INSTANCE;
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

    public <T> void upsert(@NonNull Object owner, @NonNull Object key, @NonNull T value) {
        CacheEntry entry = new CacheEntry(owner, key, value);
        try {
            upsert(entry);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void upsert(CacheEntry entity) throws SQLException {
        cacheDao().createOrUpdate(entity);
    }

    public void clear() {
        try {
            TableUtils.clearTable(getConnectionSource(), CacheEntry.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }
}