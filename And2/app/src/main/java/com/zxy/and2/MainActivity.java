package com.zxy.and2;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.GravityCompat;
import androidx.customview.widget.ViewDragHelper;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity {

    public static Fragment currentFragment = null;
    public static VolunteeringFragment volFragment;
    public static DakaFragment dakaFragment;
    public static CompanionFragment cpnFragment;
    public static Cursor cursor;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Timer timer = new Timer();

//        TimerTask timerTask1 = new TimerTask() {
//            @Override
//            public void run() {
        QMDB qmdb = new QMDB(MainActivity.this, "qm2.db", null, 1);
        SQLiteDatabase db = qmdb.getWritableDatabase();
        cursor = db.query("vol", null, null, null, null, null, null);
        if (AppControlor.USER_TYPE.equals("0")) {
            cpnFragment=new CompanionFragment();
            setFragment(cpnFragment);
        } else {
            volFragment = new VolunteeringFragment();
            setFragment(volFragment);
        }
//            }
//        };
//        timer.schedule(timerTask1, 0);


        DrawerLayout mDrawerLayout = findViewById(R.id.drawer);//获取根布局

        TimerTask timerTask2 = new TimerTask() {
            @Override
            public void run() {
                //--------------------START:使DrawerLayout可以支持全屏滑动--------------------------------
                try {
                    Field leftDraggerField = mDrawerLayout.getClass().getDeclaredField("mLeftDragger");//通过反射获得DrawerLayout类中mLeftDragger字段
                    leftDraggerField.setAccessible(true);//私有属性要允许修改
                    ViewDragHelper vdh = (ViewDragHelper) leftDraggerField.get(mDrawerLayout);//获取ViewDragHelper的实例, 通过ViewDragHelper实例获取mEdgeSize字段
                    Field edgeSizeField = vdh.getClass().getDeclaredField("mEdgeSize");//通过反射获取ViewDragHelper类中mEdgeSize字段, 这个字段控制边缘触发范围
                    edgeSizeField.setAccessible(true);//私有属性要允许修改
                    int edgeSize = edgeSizeField.getInt(vdh);//获得mEdgeSize真实值
//            Log.d("AAA", "mEdgeSize: "+edgeSize);

                    //Start 获取手机屏幕宽度，单位px
                    Point point = new Point();
                    getWindowManager().getDefaultDisplay().getRealSize(point);
                    //End 获取手机屏幕宽度

//            Log.d("AAA", "point: "+point.x);
                    edgeSizeField.setInt(vdh, Math.max(edgeSize, point.x));//这里设置mEdgeSize的值，Math.max函数取得是最大值，也可以自行指定想要触发的范围值，如: 500,注意单位是px
                    //写到这里已经实现了，但是会出现新的bug：如果长按触发范围，侧边栏也会弹出来，而且速度不太同步，稳定
//            Log.d("AAA","mEdgeSize: "+edgeSizeField.getInt(vdh));
                    //Start 解决长按会触发侧边栏
                    //长按会触发侧边栏是DrawerLayout类的私有类ViewDragCallback中的mPeekRunnable实现导致，我们通过反射把它置空
                    Field leftCallbackField = mDrawerLayout.getClass().getDeclaredField("mLeftCallback");//通过反射拿到私有类ViewDragCallback字段
                    leftCallbackField.setAccessible(true);//私有字段设置允许修改
                    ViewDragHelper.Callback vdhCallback = (ViewDragHelper.Callback) leftCallbackField.get(mDrawerLayout);//ViewDragCallback类是私有类，将返回类型定义成他的父类
                    Field peekRunnableField = vdhCallback.getClass().getDeclaredField("mPeekRunnable");//通过反射拿到关键字段mPeekRunnable
                    peekRunnableField.setAccessible(true);
                    //定义一个空的实现
                    Runnable nullRunnable = new Runnable() {
                        @Override
                        public void run() {
                        }
                    };
                    peekRunnableField.set(vdhCallback, nullRunnable);//给mPeekRunnable字段置空
                    //End 解决长按触发侧边栏
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                //--------------------END--------------------------------

                mDrawerLayout.setScrimColor(Color.parseColor("#50FEFEFE"));
                mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        View mContent = mDrawerLayout.getChildAt(0);
                        View mMenu = drawerView;
                        float scale = 1 - slideOffset;
                        float rightScale = 0.7f + scale * 0.3f;  //1~0.6
                        float leftScrale = 0.5f + slideOffset * 0.5f;
                        mMenu.setAlpha(leftScrale);
                        mMenu.setScaleX(leftScrale);
                        mMenu.setScaleY(leftScrale);
                        mContent.setPivotX(0);
                        mContent.setPivotY(mContent.getHeight() / 2);
                        mContent.setScaleX(rightScale);
                        mContent.setScaleY(rightScale);
                        mContent.setTranslationX(mMenu.getWidth() * slideOffset);
//                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                    }
                });
            }
        };
        timer.schedule(timerTask2, 500);

        TimerTask timerTask3 = new TimerTask() {
            @Override
            public void run() {
                dakaFragment = new DakaFragment();
                cpnFragment = new CompanionFragment();
            }
        };
        timer.schedule(timerTask3, 800);

        NavigationView navigationView = findViewById(R.id.navigationView);
        View converView = navigationView.getHeaderView(0);
        TextView name = converView.findViewById(R.id.header_name);
        ImageView headIcon=converView.findViewById(R.id.head_imageView);
        TextView signature=converView.findViewById(R.id.header_signature);
        SharedPreferences pref = getSharedPreferences("account", MODE_PRIVATE);
        name.setText(pref.getString("name", "姓名"));
        if(AppControlor.USER_TYPE.equals("0")){headIcon.setImageResource(R.drawable.person2);
        signature.setText(pref.getString("content","你好，启梦！").length()>8?pref.getString("content","你好，启梦！").substring(0,8)+"...":pref.getString("content","你好，启梦！"));}

        if (AppControlor.USER_TYPE.equals("0")) navigationView.inflateMenu(R.menu.menu_main2);
        else navigationView.inflateMenu(R.menu.menu_main);


//        图片圆角化
//        ImageView view = findViewById(R.id.imageView);
//        Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground2); //获取Bitmap图片
//        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), src); //创建RoundedBitmapDrawable对象
//        roundedBitmapDrawable.setCornerRadius(500); //设置圆角Radius（根据实际需求）
//        roundedBitmapDrawable.setAntiAlias(true); //设置抗锯齿
//        view.setImageDrawable(roundedBitmapDrawable); //显示圆角

    }

    public void headImgOnClick(View view) {
        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        if (!drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.openDrawer(GravityCompat.START);
    }

    public void searchImgOnclick(View view) {
        Intent intent = new Intent(this, VolSearchActivity.class);
        startActivity(intent);
    }

    public void headerOnClick(View view) {
        Intent intent = new Intent(this, PersonalActivity.class);
        startActivity(intent);
    }

    private void setFragment(Fragment fragment) {
        //如果选择的页面和当前页面一致，则什么都不干
        if (fragment == currentFragment) return;
        //获取事件
        //getSupportFragmentManager()：获取当前添加的所有fragment的Manager
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //获取当前添加的所有的Fragment，并将其隐藏起来
//        for (Fragment f : getSupportFragmentManager().getFragments()) {
//            fragmentTransaction.hide(f);
//        }
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
//            currentFragment.onPause();
        }
        //当前页面更换为选中页面
        currentFragment = fragment;
        //如果fragment没有被添加，就将其添加进来，如果已经添加，直接展示出来就好
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.main_fragment, fragment);
        } else {
//            fragment.onResume();
            fragmentTransaction.show(fragment);
        }
        //提交事件
        fragmentTransaction.commit();
    }

    @SuppressLint("NonConstantResourceId")
    public void menuOnClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.part1:
                setFragment(volFragment);
                Toolbar toolbar1 = findViewById(R.id.vol_toolbar);
                setSupportActionBar(toolbar1);
                break;
            case R.id.part2:
                setFragment(dakaFragment);
                break;
            case R.id.part3:
//                setFragment(comFragment);
                Intent intent = new Intent(MainActivity.this, CommunityActivity.class);
                startActivity(intent);
//                Toolbar toolbar3 = findViewById(R.id.cpn_toolbar);
//                setSupportActionBar(toolbar3);
                break;
            case R.id.part4:
                setFragment(cpnFragment);
                Toolbar toolbar4 = findViewById(R.id.cpn_toolbar);
                setSupportActionBar(toolbar4);
                break;
            case R.id.part5:
                Intent intent5 = new Intent(this, PersonalActivity.class);
                startActivity(intent5);
                break;
            case R.id.part6:
                Intent intent6 = new Intent(this, LoginActivity.class);
                startActivity(intent6);
                currentFragment = null;
                finish();
                break;
            case R.id.part7:
                Intent intent7 = new Intent(this, AboutActivity.class);
                startActivity(intent7);
                break;
            default:
                break;
        }
        DrawerLayout mDrawerLayout = findViewById(R.id.drawer);
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            DrawerLayout mDrawerLayout = findViewById(R.id.drawer);
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
                mDrawerLayout.closeDrawer(GravityCompat.START);
            else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
            return true;
        } else return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (currentFragment == dakaFragment) dakaFragment.onFragmentResult();
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}