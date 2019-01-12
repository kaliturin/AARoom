package com.example.anton.aaroom;

import android.annotation.SuppressLint;
import android.support.multidex.MultiDexApplication;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EApplication;

@SuppressLint("Registered")
@EApplication
public class App extends MultiDexApplication {

    @AfterInject
    public void onAfterInject() {
    }
}