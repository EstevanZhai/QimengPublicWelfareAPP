package com.zxy.and2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;


import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AppControlor {
    public static boolean LOGIN_STATE=false;
    public static String id="1";
    public static String pw="1";
    public static String USER_TYPE="1";
    public static List<Activity> activities = new ArrayList<>();
    public static MainActivity mainInstance;
    public static boolean isServerConnected=true;
    public static String serverIp="192.168.0.168";
    public static List<VolItem> volItemList;

    public static void addActivity(Activity activity){
        activities.add(activity);
        if(activity instanceof MainActivity)mainInstance= (MainActivity) activity;
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public static void finishAll(){
        for(Activity activity:activities){
            if(!activity.isFinishing())
                activity.finish();
        }
        activities.clear();
    }

    public static void loginOut(Activity activity){

    }

    public static void setStatusBarMargin(Activity activity, Toolbar toolbar){
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen","android");
        int height = resources.getDimensionPixelSize(resourceId);
        CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
        lp.setMargins(0,height,0,0);
        toolbar.setLayoutParams(lp);
    }

    public static void setNavigationBarMargin(Activity activity, View view){
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen","android");
        int height = resources.getDimensionPixelSize(resourceId);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)view.getLayoutParams();
        lp.setMargins(0,0,0,height+10);
        view.setLayoutParams(lp);
    }

    public static void initDB(Activity activity){
        @SuppressLint("SdCardPath") String path="/data/data/com.zxy.and2/databases/";
        String dbName= "qm2.db";
        File file=new File(path,dbName);
        if(file.exists())return;
        InputStream stream = null;
        FileOutputStream fos = null;
        //2,输入流读取第三方资产目录下的文件
        try {
            if(!new File(path).exists())new File(path).mkdir();
            file.createNewFile();
            stream = activity.getResources().getAssets().open(dbName);
        //3,将读取的内容写入到指定文件夹的文件中去
            fos = new FileOutputStream(file);
        //4,每次的读取内容大小
            byte[] bs = new byte[1024];
            int temp = -1;
            while( (temp = stream.read(bs))!=-1){
                fos.write(bs, 0, temp);
            }
            Toast.makeText(activity,"db start",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(stream!=null && fos!=null){
                try {
                    stream.close();
                    fos.close();
//                    Toast.makeText(activity,"db over",Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
