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


        DrawerLayout mDrawerLayout = findViewById(R.id.drawer);//???????????????

        TimerTask timerTask2 = new TimerTask() {
            @Override
            public void run() {
                //--------------------START:???DrawerLayout????????????????????????--------------------------------
                try {
                    Field leftDraggerField = mDrawerLayout.getClass().getDeclaredField("mLeftDragger");//??????????????????DrawerLayout??????mLeftDragger??????
                    leftDraggerField.setAccessible(true);//???????????????????????????
                    ViewDragHelper vdh = (ViewDragHelper) leftDraggerField.get(mDrawerLayout);//??????ViewDragHelper?????????, ??????ViewDragHelper????????????mEdgeSize??????
                    Field edgeSizeField = vdh.getClass().getDeclaredField("mEdgeSize");//??????????????????ViewDragHelper??????mEdgeSize??????, ????????????????????????????????????
                    edgeSizeField.setAccessible(true);//???????????????????????????
                    int edgeSize = edgeSizeField.getInt(vdh);//??????mEdgeSize?????????
//            Log.d("AAA", "mEdgeSize: "+edgeSize);

                    //Start ?????????????????????????????????px
                    Point point = new Point();
                    getWindowManager().getDefaultDisplay().getRealSize(point);
                    //End ????????????????????????

//            Log.d("AAA", "point: "+point.x);
                    edgeSizeField.setInt(vdh, Math.max(edgeSize, point.x));//????????????mEdgeSize?????????Math.max??????????????????????????????????????????????????????????????????????????????: 500,???????????????px
                    //???????????????????????????????????????????????????bug??????????????????????????????????????????????????????????????????????????????????????????
//            Log.d("AAA","mEdgeSize: "+edgeSizeField.getInt(vdh));
                    //Start ??????????????????????????????
                    //???????????????????????????DrawerLayout???????????????ViewDragCallback??????mPeekRunnable?????????????????????????????????????????????
                    Field leftCallbackField = mDrawerLayout.getClass().getDeclaredField("mLeftCallback");//???????????????????????????ViewDragCallback??????
                    leftCallbackField.setAccessible(true);//??????????????????????????????
                    ViewDragHelper.Callback vdhCallback = (ViewDragHelper.Callback) leftCallbackField.get(mDrawerLayout);//ViewDragCallback??????????????????????????????????????????????????????
                    Field peekRunnableField = vdhCallback.getClass().getDeclaredField("mPeekRunnable");//??????????????????????????????mPeekRunnable
                    peekRunnableField.setAccessible(true);
                    //????????????????????????
                    Runnable nullRunnable = new Runnable() {
                        @Override
                        public void run() {
                        }
                    };
                    peekRunnableField.set(vdhCallback, nullRunnable);//???mPeekRunnable????????????
                    //End ???????????????????????????
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
        name.setText(pref.getString("name", "??????"));
        if(AppControlor.USER_TYPE.equals("0")){headIcon.setImageResource(R.drawable.person2);
        signature.setText(pref.getString("content","??????????????????").length()>8?pref.getString("content","??????????????????").substring(0,8)+"...":pref.getString("content","??????????????????"));}

        if (AppControlor.USER_TYPE.equals("0")) navigationView.inflateMenu(R.menu.menu_main2);
        else navigationView.inflateMenu(R.menu.menu_main);


//        ???????????????
//        ImageView view = findViewById(R.id.imageView);
//        Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground2); //??????Bitmap??????
//        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), src); //??????RoundedBitmapDrawable??????
//        roundedBitmapDrawable.setCornerRadius(500); //????????????Radius????????????????????????
//        roundedBitmapDrawable.setAntiAlias(true); //???????????????
//        view.setImageDrawable(roundedBitmapDrawable); //????????????

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
        //???????????????????????????????????????????????????????????????
        if (fragment == currentFragment) return;
        //????????????
        //getSupportFragmentManager()??????????????????????????????fragment???Manager
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //??????????????????????????????Fragment????????????????????????
//        for (Fragment f : getSupportFragmentManager().getFragments()) {
//            fragmentTransaction.hide(f);
//        }
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
//            currentFragment.onPause();
        }
        //?????????????????????????????????
        currentFragment = fragment;
        //??????fragment???????????????????????????????????????????????????????????????????????????????????????
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.main_fragment, fragment);
        } else {
//            fragment.onResume();
            fragmentTransaction.show(fragment);
        }
        //????????????
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