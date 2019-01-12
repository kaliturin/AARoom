package com.example.anton.aaroom.ui.main.cache.ormlite;

import android.support.annotation.NonNull;

import com.example.anton.aaroom.ui.main.cache.CacheEntry;
import com.example.anton.aaroom.ui.main.cache.CacheSerializer;
import com.j256.ormlite.field.DatabaseField;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@SuppressWarnings("WeakerAccess")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OrmLiteCacheEntry extends CacheEntry {
    public static final int VERSION = 1;    // increment on the entry's fields changing

    @DatabaseField
    @NonNull
    private String owner = "";  // cache entry owner
    @DatabaseField
    @NonNull
    private String key = "";    // cache entry key
    @DatabaseField
    private String value;       // serialized value
    @DatabaseField
    private String type;        // serialized type of the value
    @DatabaseField
    private long time;          // cache entry creation time
    @DatabaseField
    private String tag;         // cache entry tag

    // OrmLite doesn't support the composite primary key (Room does),
    // so we'll make the primary key concatenating the owner and key
    @DatabaseField(id = true)
    private String id;

    public OrmLiteCacheEntry(String owner, String key, String value, String type, long time, String tag) {
        this.owner = owner == null ? "" : owner;
        this.key = key == null ? "" : key;
        this.value = value;
        this.type = type;
        this.time = time;
        this.tag = tag;
        this.id = buildId(owner, key);
    }

    public OrmLiteCacheEntry(CacheSerializer serializer, Object owner, Object key, Object value, long time, String tag) {
        this(serializer.serializeOwner(owner, null),
                serializer.serializeKey(key, null),
                serializer.serializeValue(value),
                serializer.serializeType(value, null),
                time,
                tag);
    }

    public OrmLiteCacheEntry(CacheEntry entry) {
        this(entry.getOwner(), entry.getKey(), entry.getValue(),
                entry.getType(), entry.getTime(), entry.getTag());
    }

    public static String buildId(CacheSerializer serializer, Object owner, Object key) {
        return buildId(serializer.serializeOwner(owner, null),
                serializer.serializeKey(key, null));
    }

    public static String buildId(String owner, String key) {
        return (owner == null ? "" : owner) + " " + (key == null ? "" : key);
    }
}
