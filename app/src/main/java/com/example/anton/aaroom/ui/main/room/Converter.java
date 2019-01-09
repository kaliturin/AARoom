package com.example.anton.aaroom.ui.main.room;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.anton.aaroom.ui.main.JsonUtils;

@SuppressWarnings("WeakerAccess")
public class Converter {

    @Nullable
    public static <T> T fromString(@Nullable String string, @NonNull Class<T> clazz) {
        return JsonUtils.fromJson(string, clazz);
    }

    @Nullable
    public static String toString(@Nullable Object object) {
        return JsonUtils.toJson(object);
    }
}
