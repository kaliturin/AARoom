package com.example.anton.aaroom.ui.main.persistent.cache.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.anton.aaroom.ui.main.persistent.cache.CacheEntry;
import com.example.anton.aaroom.ui.main.persistent.cache.CacheSerializer;
import com.example.anton.aaroom.ui.main.persistent.cache.CacheStorage;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import timber.log.Timber;

/**
 * OrmLite cache storage
 */
public class OrmLiteCacheStorage extends OrmLiteSqliteOpenHelper implements CacheStorage {

    // The storage type identifier
    public static final int TYPE = 1;

    private Dao<OrmLiteCacheEntry, String> cacheDao;

    // Default cache entry fields serializer
    @Getter
    @Setter
    private CacheSerializer serializer = new CacheSerializer();

    private OrmLiteCacheStorage(Context context, String name) {
        super(context, name, null, OrmLiteCacheEntry.VERSION);
    }

    /**
     * Creates the new instance of the cache storage.
     * Normally, this method shouldn't be called more than 1 time with the same parameters.
     */
    @NonNull
    public static CacheStorage newInstance(Context context, String name) {
        return new OrmLiteCacheStorage(context.getApplicationContext(), name);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, OrmLiteCacheEntry.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, OrmLiteCacheEntry.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Dao<OrmLiteCacheEntry, String> cacheDao() throws SQLException {
        if (cacheDao == null) {
            cacheDao = getDao(OrmLiteCacheEntry.class);
        }
        return cacheDao;
    }

    @Override
    public void close() {
        cacheDao = null;
        super.close();
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public void deleteAll() {
        try {
            TableUtils.clearTable(getConnectionSource(), OrmLiteCacheEntry.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteByOwnerAndTag(Object owner, String tag) {
        String _owner = serializer.serializeOwner(owner, "");
        DeleteBuilder<OrmLiteCacheEntry, String> deleteBuilder = cacheDao.deleteBuilder();
        try {
            deleteBuilder.where()
                    .eq("owner", _owner)
                    .and()
                    .eq("tag", tag);
            deleteBuilder.delete();
        } catch (SQLException e) {
            Timber.e(e);
        }
    }

    @Override
    public void deleteByOwnerAndTagAndKeyNotIn(Object owner, String tag, List<Object> keys) {
        String _owner = serializer.serializeOwner(owner, "");
        List<String> _keys = new ArrayList<>();
        for (Object key : keys) {
            _keys.add(serializer.serializeKey(key, ""));
        }
        DeleteBuilder<OrmLiteCacheEntry, String> deleteBuilder = cacheDao.deleteBuilder();
        try {
            deleteBuilder.where()
                    .eq("owner", _owner)
                    .and()
                    .eq("tag", tag)
                    .and()
                    .not()
                    .in("key", _keys);
            deleteBuilder.delete();
        } catch (SQLException e) {
            Timber.e(e);
        }
    }

    @Override
    public void deleteByOwner(Object owner) {
        String _owner = serializer.serializeOwner(owner, "");
        DeleteBuilder<OrmLiteCacheEntry, String> deleteBuilder = cacheDao.deleteBuilder();
        try {
            deleteBuilder.where()
                    .eq("owner", _owner);
            deleteBuilder.delete();
        } catch (SQLException e) {
            Timber.e(e);
        }
    }

    @Override
    public void deleteByTag(String tag) {
        DeleteBuilder<OrmLiteCacheEntry, String> deleteBuilder = cacheDao.deleteBuilder();
        try {
            deleteBuilder.where()
                    .eq("tag", tag);
            deleteBuilder.delete();
        } catch (SQLException e) {
            Timber.e(e);
        }
    }

    @Override
    public void delete(CacheEntry entry) {
        OrmLiteCacheEntry _entry = new OrmLiteCacheEntry(entry);
        try {
            cacheDao().delete(_entry);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy(Context context) {
        Timber.d("Destroying persistent cache: " + getDatabaseName());
        close();
        context.deleteDatabase(getDatabaseName());
    }

    @Override
    public void setEntry(@NonNull CacheEntry entry) {
        OrmLiteCacheEntry _entry = new OrmLiteCacheEntry(entry);
        try {
            cacheDao().createOrUpdate(_entry);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Nullable
    public CacheEntry getEntry(Object owner, Object key) {
        String id = OrmLiteCacheEntry.buildId(serializer, owner, key);
        try {
            return cacheDao().queryForId(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> void setValue(Object owner, Object key, T value, long time, String tag) {
        OrmLiteCacheEntry entry = new OrmLiteCacheEntry(serializer, owner, key, value, time, tag);
        try {
            cacheDao().createOrUpdate(entry);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}