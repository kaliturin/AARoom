package com.example.anton.aaroom.ui.main.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("WeakerAccess")
@Entity(primaryKeys = {"owner", "key"})
@Data
@NoArgsConstructor
public class CacheEntry {

    @NonNull
    public String owner = "";
    @NonNull
    public String key = "";
    public String value;
    @TypeConverters({ClazzConverters.class})
    public Class clazz;

    public CacheEntry(@NonNull Object owner, @NonNull Object key, @NonNull Object value) {
        String _owner = owner.getClass().getCanonicalName();
        this.owner = _owner == null ? "" : _owner;
        this.key = key.toString();
        this.value = Converter.toString(value);
        this.clazz = value.getClass();
    }

    @Ignore
    @Nullable
    public <T> T getValue() {
        Class<T> clazz = getClazz();
        return clazz == null ? null : Converter.fromString(value, clazz);
    }

    @Ignore
    @SuppressWarnings("unchecked")
    @Nullable
    private <T> Class<T> getClazz() {
        try {
            return (Class<T>) clazz;
        } catch (ClassCastException ignored) {
            return null;
        }
    }

    public static class ClazzConverters {
        @TypeConverter
        @Nullable
        public static Class fromString(String name) {
            try {
                return name == null ? null : Class.forName(name);
            } catch (ClassNotFoundException ignored) {
                return null;
            }
        }

        @TypeConverter
        @Nullable
        public static String toString(Class clazz) {
            return clazz == null ? null : clazz.getCanonicalName();
        }
    }
}
