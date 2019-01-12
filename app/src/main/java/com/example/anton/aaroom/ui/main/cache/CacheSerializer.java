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
 * If you want to use another way
 * of serialization - just override <i>serializeOwner</i> method. For example if you want to use
 * <i>Strings</i> as <i>owner</i> instead of <i>this</i> - just do like that:
 * <p>
 * <pre><i>
 * cacheStorage.setSerializer(new CacheSerializer() {
 *  public String serializeOwner(Object owner, String onNull) {
 *      return owner == null ? onNull : owner.toString();
 *  }
 * });
 * </i></pre>
 * <p>
 * Then you can do like that:
 * <pre><i>
 * cacheStorage.getEntry("my_owner", "my_key");
 * </i></pre>
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
        String s = owner == null ? null : owner.getClass().getCanonicalName();
        // getCanonicalName is nullable!
        return s == null ? onNull : s;
    }

    @Nullable
    public String serializeKey(Object key, String onNull) {
        return key == null ? onNull : key.toString();
    }
}
