package com.zxy.and2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.Activity;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

public class VolSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vol_search);

        SearchView searchView = findViewById(R.id.vol_searchview);
        searchView.requestFocus();
    }
}