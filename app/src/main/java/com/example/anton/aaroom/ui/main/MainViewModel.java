package com.example.anton.aaroom.ui.main;

import android.arch.lifecycle.ViewModel;

import com.example.anton.aaroom.Config;
import com.example.anton.aaroom.ui.main.persistent.cache.CacheEntry;
import com.example.anton.aaroom.ui.main.persistent.cache.CacheStorage;
import com.example.anton.aaroom.ui.main.persistent.cache.CacheStorageManager;

public class MainViewModel extends ViewModel {

    private CacheStorage cache = CacheStorageManager.instance(Config.CACHE_MODELS_NAME);

    public void setValue(Object key, Item item, String tag) {
        cache.setValue(this, key, item, tag);
    }

    public Item getValue(Object key) {
        return cache.getValue(this, key);
    }

    public void deleteAll() {
        cache.deleteAll();
    }

    public void deleteBy(String tag) {
        cache.deleteByOwnerAndTag(this, tag);
    }

    public void destroy() {
        CacheStorageManager.destroy(Config.CACHE_MODELS_NAME);
    }

    public CacheEntry getEntry(Object key) {
        return cache.getEntry(this, key);
    }

    public void deleteEntry(CacheEntry entry) {
        cache.delete(entry);
    }
}
