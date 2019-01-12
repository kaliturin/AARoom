package com.example.anton.aaroom.ui.main.cache.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import com.example.anton.aaroom.ui.main.cache.CacheEntry;
import com.example.anton.aaroom.ui.main.cache.CacheSerializer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@SuppressWarnings("WeakerAccess")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity(primaryKeys = {"owner", "key"})
public class RoomCacheEntry extends CacheEntry {
    public static final int VERSION = 1;    // increment on the entry's fields changing

    @NonNull
    public String owner = "";   // cache entry owner
    @NonNull
    public String key = "";     // cache entry key
    public String value;        // serialized value
    public String type;         // serialized type of the value
    public long time;           // cache entry creation time
    public String tag;          // cache entry tag

    @Ignore
    public RoomCacheEntry(String owner, String key, String value, String type, long time, String tag) {
        this.owner = owner == null ? "" : owner;
        this.key = key == null ? "" : key;
        this.value = value;
        this.type = type;
        this.time = time;
        this.tag = tag;
    }

    @Ignore
    public RoomCacheEntry(CacheSerializer serializer, Object owner, Object key, Object value, long time, String tag) {
        this(serializer.serializeOwner(owner, null),
                serializer.serializeKey(key, null),
                serializer.serializeValue(value),
                serializer.serializeType(value, null),
                time,
                tag);
    }

    @Ignore
    public RoomCacheEntry(CacheEntry entry) {
        this(entry.getOwner(), entry.getKey(), entry.getValue(),
                entry.getType(), entry.getTime(), entry.getTag());
    }
}
