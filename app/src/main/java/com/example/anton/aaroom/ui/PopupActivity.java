package com.example.anton.aaroom.ui;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;

import com.example.anton.aaroom.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.main_activity)
@SuppressLint("Registered")
public class PopupActivity extends AppCompatActivity {

    @AfterViews
    protected void onAfterViews() {
    }
}
