package com.example.anton.aaroom.ui.main.persistent.cache.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface CacheDao {

    @Query("SELECT * FROM RoomCacheEntry WHERE owner IS :owner AND `key` IS :key")
    RoomCacheEntry select(String owner, String key);

    @Delete
    void delete(RoomCacheEntry entry);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(RoomCacheEntry entity);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(RoomCacheEntry entity);

    @Query("DELETE FROM RoomCacheEntry")
    void deleteAll();

    @Query("DELETE FROM RoomCacheEntry WHERE owner IS :owner AND tag IS :tag")
    void deleteByOwnerAndTag(String owner, String tag);

    @Query("DELETE FROM RoomCacheEntry WHERE owner IS :owner AND tag IS :tag AND `key` NOT IN(:keys)")
    void deleteByOwnerAndTagAndKeyNotIn(String owner, String tag, List<String> keys);

    @Query("DELETE FROM RoomCacheEntry WHERE owner IS :owner")
    void deleteByOwner(String owner);

    @Query("DELETE FROM RoomCacheEntry WHERE tag IS :tag")
    void deleteByTag(String tag);
}
