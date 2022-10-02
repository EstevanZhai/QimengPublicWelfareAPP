package com.zxy.and2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {
    private SharedPreferences pref;
    private String realName;
    private String realPw;
    private String type;
    private OkHttpClient client = new OkHttpClient();
    private RealInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LinearLayout linearLayout = findViewById(R.id.vol_mengban);
        linearLayout.setVisibility(View.GONE);

//        Intent intent = getIntent();
//        int data = intent.getIntExtra("START_ANIMATION",0);
//        if(data==1)
//            AppStartAnimation.appStartAnimation(this);

        //点击空白处清除焦点
        EditText account = findViewById(R.id.account);
        EditText password = findViewById(R.id.password);
        View bg = findViewById(R.id.view);
        bg.setFocusable(true);
        bg.setFocusableInTouchMode(true);
        InputMethodManager inputMethodManager = (InputMethodManager) LoginActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);//键盘弹出收回管理器

        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bg.requestFocus();
//                inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);//自动弹出键盘
//                inputMethodManager.hideSoftInputFromWindow(account.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        bg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                inputMethodManager.hideSoftInputFromWindow(account.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                inputMethodManager.hideSoftInputFromWindow(password.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        TextView register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, PreEnrollActivity.class);
                startActivity(intent);
            }
        });

        TextView connect = findViewById(R.id.connect_us);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "联系方式是个秘密~", Toast.LENGTH_SHORT).show();
            }
        });

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                pref = getSharedPreferences("account", MODE_PRIVATE);
                realName = pref.getString("name", null);
                realPw = pref.getString("pw", null);
                type=pref.getString("type",null);

            }
        };
        timer.schedule(timerTask, 0);

        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    public void login() {
        EditText account = (EditText) findViewById(R.id.account);
        if (AppControlor.isServerConnected&&account.getText().toString().length()>0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        RequestBody rb = new FormBody.Builder().add("name", account.getText().toString()).build();
                        Request request = new Request.Builder().url("http://" + AppControlor.serverIp + "/login").post(rb).build();
                        Response response = client.newCall(request).execute();
                        Gson gson = new Gson();
                        info = gson.fromJson(response.body().string(), RealInfo.class);
                        ConstraintLayout constraintLayout=findViewById(R.id.login_activity);
                        constraintLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                if(info.name.equals(""))Toast.makeText(LoginActivity.this,"账号不存在",Toast.LENGTH_SHORT).show();
                                else login(info.name,info.pw,info.type);
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            login(realName,realPw,type);
        }
    }

    private void login(String realName, String realPw,String type){
        if (realName == null || realPw == null) {
            Toast.makeText(this, "暂无账号，请先注册", Toast.LENGTH_SHORT).show();
            return;
        }
        EditText account = (EditText) findViewById(R.id.account);
        EditText password = findViewById(R.id.password);
        String name = account.getText().toString();
        String pw = password.getText().toString();
        if (name.equals(realName) && pw.equals(realPw)) {
            AppControlor.id = realName;
            AppControlor.USER_TYPE=type;
            LinearLayout linearLayout = findViewById(R.id.vol_mengban);
            linearLayout.setVisibility(View.VISIBLE);
            if(info!=null) {
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = pref.edit();
                editor.putString("name", info.name);
                editor.putString("pw", info.pw);
                editor.putString("age", info.age);
                editor.putString("college", info.college);
                editor.putString("content", info.content);
                editor.putString("type", info.type);
                if (info.type.equals("1")) {
                    editor.putString("job", info.job);
                    editor.putString("years", info.years);
                }
                editor.apply();
            }
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else if (name.length() == 0) {
            Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
            this.realName = pref.getString("name", null);
            this.realPw = pref.getString("pw", null);
            this.type=pref.getString("type",null);
            account.setText(this.realName);
            password.setText(this.realPw);
        } else if (pw.length() == 0) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "账号/密码出错，请重新输入", Toast.LENGTH_SHORT).show();
    }

//    public void connectUs(View v) {
//        startActivity(new Intent(this,ConnectUs.class));
//    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        } else return super.onKeyDown(keyCode, event);
    }
}

class RealInfo {
    String name;
    String pw;
    String type;
    String age;
    String job;
    String college;
    String years;
    String content;
}