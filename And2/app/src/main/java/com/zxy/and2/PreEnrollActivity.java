package com.zxy.and2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PreEnrollActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_enroll);

        Button bt1=findViewById(R.id.pre_enroll_button1);
        Button bt2=findViewById(R.id.pre_enroll_button2);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PreEnrollActivity.this,EnrollActivity.class);
                startActivity(intent);
                finish();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PreEnrollActivity.this,EnrollActivity2.class);
                startActivity(intent);
                finish();
            }
        });
    }
}