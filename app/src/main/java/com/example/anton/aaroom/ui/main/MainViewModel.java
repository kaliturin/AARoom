package com.example.anton.aaroom.ui.main;

import android.arch.lifecycle.ViewModel;

import com.example.anton.aaroom.Config;
import com.example.anton.aaroom.ui.main.cache.CacheStorage;
import com.example.anton.aaroom.ui.main.cache.CacheStorageManager;

public class MainViewModel extends ViewModel {

    private CacheStorage cache = CacheStorageManager.instance(Config.CACHE_MODELS_NAME);

    public void setValue(Object key, Item item) {
        cache.setValue("this", key, item);
    }

    public Item getValue(Object key) {
        return cache.getValue("this", key);
    }

    public void deleteAll() {
        cache.deleteAll();
    }

    public void destroy() {
        CacheStorageManager.destroy(Config.CACHE_MODELS_NAME);
    }
}
