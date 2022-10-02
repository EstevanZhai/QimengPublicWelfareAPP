package com.zxy.and2;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity",getClass().getSimpleName());
        AppControlor.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppControlor.removeActivity(this);
        if(this instanceof MainActivity)AppControlor.mainInstance=null;
    }

}
