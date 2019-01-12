package com.example.anton.aaroom.ui.main.cache;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.anton.aaroom.ui.main.JsonUtils;

/**
 * Cache entry fields serializer.
 * All fields of the cache entry which have complex types must be serialized to <i>String</i>
 * in order to write them into a cache storage.
 * <p>
 * By default we serialize the <i>Object owner</i> like that: <i>owner.getClass().getCanonicalName()</i>
 * in order to use <i>this</i> keyword as an <i>owner</i> parameter:
 * <pre><i>
 * cacheStorage.getEntry(this, "my_key");
 * </i></pre>
 * <p>
 * Also you can do like that:
 * <pre><i>
 * cacheStorage.getEntry("my_owner", "my_key");
 * </i></pre>
 * In that way string "my_owner" will be used without additional serialization.
 */
public class CacheSerializer {

    @Nullable
    public String serializeValue(@Nullable Object object) {
        return JsonUtils.toJson(object);
    }

    @Nullable
    public <T> T deserializeValue(@Nullable String string, @NonNull Class<T> clazz) {
        return JsonUtils.fromJson(string, clazz);
    }

    @Nullable
    public String serializeType(Object type, String onNull) {
        String s = type == null ? null : type.getClass().getCanonicalName();
        // getCanonicalName is nullable!
        return s == null ? onNull : s;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> Class<T> deserializeType(String type) {
        try {
            return TextUtils.isEmpty(type) ? null : (Class<T>) Class.forName(type);
        } catch (ClassNotFoundException | ClassCastException ignored) {
            return null;
        }
    }

    @Nullable
    public String serializeOwner(Object owner, String onNull) {
        // use strings as is
        if (owner instanceof String) {
            return (String) owner;
        }
        // serialize the other types as class name
        String s = owner == null ? null : owner.getClass().getCanonicalName();
        // getCanonicalName is nullable!
        return s == null ? onNull : s;
    }

    @Nullable
    public String serializeKey(Object key, String onNull) {
        return key == null ? onNull : key.toString();
    }
}
