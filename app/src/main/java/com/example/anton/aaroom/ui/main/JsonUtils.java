package com.example.anton.aaroom.ui.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Json conversion facilities
 */
@SuppressWarnings("WeakerAccess")
public class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Converts passed object to json string
     */
    @Nullable
    public static String toJson(@Nullable Object object) {
        String json;
        try {
            json = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return json;
    }

    /**
     * Converts passed json string to object
     */
    @Nullable
    public static <T> T fromJson(@Nullable String json, @NonNull Class<T> clazz) {
        if (!TextUtils.isEmpty(json)) {
            try {
                return mapper.readerFor(clazz).readValue(json);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
