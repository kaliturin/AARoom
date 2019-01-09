package com.example.anton.aaroom.ui.main.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface CacheDao {

    @Query("SELECT * FROM CacheEntry WHERE `owner` IS :owner AND `key` IS :key")
    CacheEntry select(String owner, String key);

    @Delete
    void delete(CacheEntry entry);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(CacheEntry entity);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(CacheEntry entity);

    @Query("DELETE FROM CacheEntry")
    void deleteAll();
}
