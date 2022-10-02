package com.zxy.and2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EnrollActivity extends BaseActivity {

    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll);

        Button enroll = findViewById(R.id.enroll);
        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText name = findViewById(R.id.edit_enroll_name);
                EditText pw = findViewById(R.id.edit_enroll_pw);
                EditText age = findViewById(R.id.edit_enroll_age);
                EditText job = findViewById(R.id.edit_enroll_job);
                EditText college = findViewById(R.id.edit_enroll_college);
                EditText years = findViewById(R.id.edit_enroll_years);
                EditText content = findViewById(R.id.edit_enroll_content);

                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = getSharedPreferences("account", MODE_PRIVATE).edit();
                editor.putString("name", name.getText().toString());
                editor.putString("pw", pw.getText().toString());
                editor.putString("age", age.getText().toString());
                editor.putString("job", job.getText().toString());
                editor.putString("college", college.getText().toString());
                editor.putString("years", years.getText().toString());
                editor.putString("content", content.getText().toString());
                editor.putString("type", "1");
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
                                    .add("job", job.getText().toString())
                                    .add("college", college.getText().toString())
                                    .add("years", years.getText().toString())
                                    .add("content", content.getText().toString())
                                    .add("type", "1")
                                    .add("online", "false")
                                    .build();
                            Request request = new Request.Builder().url("http://" + AppControlor.serverIp + "/enroll").post(rb).build();
                            client.newCall(request).execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                AppControlor.id=name.getText().toString();
                Toast.makeText(EnrollActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EnrollActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}