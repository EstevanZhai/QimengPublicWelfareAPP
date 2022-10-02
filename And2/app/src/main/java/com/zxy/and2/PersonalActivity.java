package com.zxy.and2;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxy.and2.databinding.ActivityPersonalBinding;

public class PersonalActivity extends BaseActivity {

//    private ActivityPersonalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        Toolbar toolbar = findViewById(R.id.personal_toolbar);
        setSupportActionBar(toolbar);

        Button back = findViewById(R.id.person_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        SharedPreferences pref =getSharedPreferences("account",MODE_PRIVATE);

        ImageView imageView=findViewById(R.id.person_bg);
        TextView job=findViewById(R.id.personal_job);
        TextView age=findViewById(R.id.personal_age);
        TextView college=findViewById(R.id.personal_college);
        TextView years=findViewById(R.id.personal_years);
        TextView content=findViewById(R.id.personal_content);

        toolbar.setTitle(pref.getString("name","姓名"));
        if(AppControlor.USER_TYPE.equals("0"))imageView.setImageResource(R.drawable.person2);
        if(AppControlor.USER_TYPE.equals("1"))job.setText("职业："+pref.getString("job","职业"));
        else job.setVisibility(View.GONE);
        age.setText("年龄："+pref.getString("age","年龄"));
        college.setText("毕业院校："+pref.getString("college","毕业院校"));
        if(AppControlor.USER_TYPE.equals("1"))years.setText("志愿年限："+pref.getString("years","志愿年限"));
        else years.setVisibility(View.GONE);
        content.setText("个人介绍："+pref.getString("content","个人介绍"));


//        if(getSupportActionBar()!=null)getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        binding = ActivityPersonalBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());

//        Toolbar toolbar = binding.personalToolbar;
//        setSupportActionBar(toolbar);
//        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
//        toolBarLayout.setTitle(getTitle());

//        FloatingActionButton fab = binding.fab;
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }
}