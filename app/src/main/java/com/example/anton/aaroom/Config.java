package com.example.anton.aaroom;

import com.example.anton.aaroom.ui.main.cache.CacheStorageManager;

public class Config {

    public static final String CACHE_MODELS_NAME = "cache-models.db";
    public static final int CACHE_MODELS_TYPE = CacheStorageManager.ORM_LITE;
    public static final long CACHE_MODELS_NEWS_EXPIRATION_TIMEOUT_SEC = 3600;
}
