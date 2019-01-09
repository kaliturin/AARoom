package com.example.anton.aaroom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.anton.aaroom.ui.main.MainFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
    }
}
