package com.example.anton.aaroom.ui.main.ormlite;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.anton.aaroom.ui.main.room.Converter;
import com.j256.ormlite.field.DatabaseField;

import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("WeakerAccess")
@NoArgsConstructor
@Data
public class CacheEntry {

    @DatabaseField
    private String owner;
    @DatabaseField
    private String key;
    @DatabaseField
    private String value;
    @DatabaseField
    private String clazz;

    // OrmLite doesn't support composite primary key, so make the key concatenating the owner and key
    @DatabaseField(id = true)
    private String id;

    public CacheEntry(@NonNull Object owner, @NonNull Object key, @NonNull Object value) {
        String _owner = owner.getClass().getCanonicalName();
        this.owner = _owner == null ? "" : _owner;
        this.key = key.toString();
        this.value = Converter.toString(value);
        this.clazz = value.getClass().getCanonicalName();
        this.id = buildId(owner, key);
    }

    @Nullable
    public <T> T getValue() {
        Class<T> clazz = getClazz();
        return clazz == null ? null : Converter.fromString(value, clazz);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private <T> Class<T> getClazz() {
        try {
            return clazz == null ? null : (Class<T>) Class.forName(clazz);
        } catch (ClassNotFoundException | ClassCastException ignored) {
            return null;
        }
    }

    public static String buildId(@NonNull Object owner, @NonNull Object key) {
        String _owner = owner.getClass().getCanonicalName();
        return (_owner == null ? "" : _owner) + "/" + key.toString();
    }
}
