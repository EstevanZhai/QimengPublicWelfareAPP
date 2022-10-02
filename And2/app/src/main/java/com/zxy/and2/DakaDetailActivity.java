package com.zxy.and2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class DakaDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daka_detail);

        TextView textView=findViewById(R.id.daka_detail_text);
        ImageView imageView=findViewById(R.id.daka_detail_image);
        TextView dateView=findViewById(R.id.daka_detail_date);

        SharedPreferences pref=getSharedPreferences(AppControlor.id+"_photo",MODE_PRIVATE);
        Intent intent=getIntent();
        int position=pref.getInt("sum",1)- intent.getIntExtra("position",0);
        String text=pref.getString("content"+position,"文本");
        String imagePath=pref.getString("image"+position,null);
        String date=pref.getString("date"+position,null);
        textView.setText(text);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        imageView.setImageBitmap(bitmap);
        dateView.setText(date);
    }
}