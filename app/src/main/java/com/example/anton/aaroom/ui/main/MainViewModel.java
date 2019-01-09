package com.example.anton.aaroom.ui.main;

import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.example.anton.aaroom.ui.main.ormlite.CacheDatabase;
//import com.example.anton.aaroom.ui.main.room.CacheDatabase;

public class MainViewModel extends ViewModel {

    public void initDatabase(Context context) {
        CacheDatabase.instance(context);
    }

    public void upsertItem(Object key, Item item) {
        CacheDatabase.instance().upsert(this, key, item);
    }

    public Item selectItem(Object key) {
        return CacheDatabase.instance().select(this, key);
    }

    public void clearItems() {
        CacheDatabase.instance().clear();
    }

    public void deleteDatabase(Context context) {
        CacheDatabase.instance().deleteDatabase(context);
    }
}
