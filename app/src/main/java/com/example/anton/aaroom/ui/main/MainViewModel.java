package com.example.anton.aaroom.ui.main;

import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.example.anton.aaroom.ui.main.cache.CacheStorageManager;

public class MainViewModel extends ViewModel {

    public void init(Context context) {
        CacheStorageManager.instance(context,
                CacheStorageManager.ROOM, "model-cache.db");
    }

    public void upsert(Object key, Item item) {
        CacheStorageManager.instance().upsert(this, key, item);
    }

    public Item select(Object key) {
        return CacheStorageManager.instance().select(this, key);
    }

    public void clear() {
        CacheStorageManager.instance().clear();
    }

    public void destroy(Context context) {
        CacheStorageManager.instance().destroy(context);
        CacheStorageManager.destroyInstance();
    }
}
