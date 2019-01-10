package com.example.anton.aaroom.ui.main.cache;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.anton.aaroom.ui.main.JsonUtils;

public class CacheSerializer {

    @Nullable
    public static <T> T deserialize(@Nullable String string, @NonNull Class<T> clazz) {
        return JsonUtils.fromJson(string, clazz);
    }

    @Nullable
    public static String serialize(@Nullable Object object) {
        return JsonUtils.toJson(object);
    }
}
