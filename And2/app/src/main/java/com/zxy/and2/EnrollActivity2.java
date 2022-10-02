package com.zxy.and2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class EnrollActivity2 extends BaseActivity {

    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll2);

        Button enroll = findViewById(R.id.enroll2);
        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText name = findViewById(R.id.edit_enroll_name2);
                EditText pw = findViewById(R.id.edit_enroll_pw2);
                EditText age = findViewById(R.id.edit_enroll_age2);
                EditText college = findViewById(R.id.edit_enroll_college2);
                EditText content = findViewById(R.id.edit_enroll_content2);

                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = getSharedPreferences("account", MODE_PRIVATE).edit();
                editor.putString("name", name.getText().toString());
                editor.putString("pw", pw.getText().toString());
                editor.putString("age", age.getText().toString());
                editor.putString("college", college.getText().toString());
                editor.putString("content", content.getText().toString());
                editor.putString("type", "0");
                editor.putBoolean("online", false);
                editor.apply();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            RequestBody rb = new FormBody.Builder()
                                    .add("name", name.getText().toString())
                                    .add("pw", pw.getText().toString())
                                    .add("age", age.getText().toString())
                                    .add("college", college.getText().toString())
                                    .add("content", content.getText().toString())
                                    .add("type", "0")
                                    .add("online", "false")
                                    .build();
                            Request request = new Request.Builder().url("http://" + AppControlor.serverIp + "/enroll").post(rb).build();
                            client.newCall(request).execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }}).start();
                AppControlor.id=name.getText().toString();
                Toast.makeText(EnrollActivity2 .this,"注册成功！",Toast.LENGTH_SHORT).

                    show();

                    Intent intent = new Intent(EnrollActivity2.this, LoginActivity.class);

                    startActivity(intent);

                    finish();
                }
            });
        }
    }