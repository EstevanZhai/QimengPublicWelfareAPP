package com.zxy.and2;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;

//import site.gemus.openingstartanimation.DrawStrategy;
//import site.gemus.openingstartanimation.LineDrawStrategy;
//import site.gemus.openingstartanimation.NormalDrawStrategy;
//import site.gemus.openingstartanimation.RedYellowBlueDrawStrategy;
//import site.gemus.openingstartanimation.RotationDrawStrategy;
//import site.gemus.openingstartanimation.WidthAndHeightOfView;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zxy.and2.placeholder.VolPlaceholderContent;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StartActivity extends BaseActivity {

    public static OkHttpClient client=new OkHttpClient.Builder().connectTimeout(1400, TimeUnit.MILLISECONDS).build();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if(AppControlor.activities.size()>1){
            finish();
            return;
        }

        AppStartAnimation.appStartAnimation(this);

        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent;
                if (!AppControlor.LOGIN_STATE)
                    intent = new Intent(StartActivity.this, LoginActivity.class);
                else
                    intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
//                overridePendingTransition(R.anim.trans_in,R.anim.trans_out);
                finish();
            }
        };
        timer.schedule(task, 1500);

        if(AppControlor.isServerConnected){
            TimerTask task2=new TimerTask() {
                @Override
                public void run() {
                    try {
                        Request request=new Request.Builder().url("http://"+AppControlor.serverIp+"/vol_items").build();
                        Response response =client.newCall(request).execute();
                        Gson gson = new Gson();
                        AppControlor.volItemList=gson.fromJson(response.body().string(),new TypeToken<List<VolItem>>(){}.getType());
                        for(VolItem volItem:AppControlor.volItemList) {
                            volItem.setSubtitle(volItem.getContent().length()>39?volItem.getContent().substring(0, 39) + "...":volItem.getContent()+"...");
                            volItem.setImage(Glide.with(getBaseContext()).load("http://"+AppControlor.serverIp+"/vol_image?id="+volItem.getSrc()).submit().get());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        AppControlor.isServerConnected=false;
                    }
                }
            };
            timer.schedule(task2,0);
        }else {
            TimerTask task1 = new TimerTask() {
                @Override
                public void run() {
                    AppControlor.initDB(StartActivity.this);
                    QMDB qmdb = new QMDB(StartActivity.this, "qm2.db", null, 1);
                    qmdb.getWritableDatabase().close();
                }
            };
            timer.schedule(task1, 100);
        }
    }
}