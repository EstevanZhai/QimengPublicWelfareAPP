package com.zxy.and2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class VolDetailActivity extends BaseActivity {
    SQLiteDatabase db;
    @SuppressLint("Recycle")
    Cursor cursor;
    String title;
    OkHttpClient client = new OkHttpClient();
    VolItem volItem;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vol_detail);

        AppControlor.setStatusBarMargin(this, findViewById(R.id.vol_detail_toolbar));

        Toolbar toolbar = findViewById(R.id.vol_detail_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageView imageView = findViewById(R.id.vol_detail_image);
        TextView addressView = findViewById(R.id.vol_detail_address);
        TextView dateView = findViewById(R.id.vol_detail_date);
        TextView contentView = findViewById(R.id.vol_detail_content);
        TextView phoneView = findViewById(R.id.vol_detail_phone);
        Button button1 = findViewById(R.id.vol_detail_button1);
        Button button2 = findViewById(R.id.vol_detail_button2);

        title = getIntent().getStringExtra("title");
        int position = getIntent().getIntExtra("position", 0);

        Drawable image = null;
        String address = "\n", date = "志愿日期：", content = "\n", phone = "志愿活动联系方式：";
        int state = 0;

        if (AppControlor.isServerConnected && AppControlor.volItemList!=null&&position < AppControlor.volItemList.size()) {
            volItem = AppControlor.volItemList.get(position);
            image = volItem.getImage();
            address = volItem.getAddress();
            date = "志愿日期：" + volItem.getDate();
            content = "\u3000\u3000" + volItem.getContent();
            phone = "志愿活动联系方式：" + volItem.getPhone();
            state = Integer.parseInt(volItem.getState());
        } else {
            QMDB qmdb = new QMDB(this, "qm2.db", null, 1);
            db = qmdb.getWritableDatabase();
            cursor = db.query("vol", new String[]{"title", "content", "image", "address", "date", "phone", "state"}, "title=?", new String[]{title}, null, null, null);
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") String imageString = cursor.getString(cursor.getColumnIndex("image"));
                if (imageString != null) {
                    byte[] imageCode = Base64.decode(imageString, Base64.DEFAULT);
                    Bitmap bmpout = BitmapFactory.decodeByteArray(imageCode, 0, imageCode.length);
                    image = new BitmapDrawable(Resources.getSystem(), bmpout);
                } else
                    image = ResourcesCompat.getDrawable(this.getResources(), R.drawable.ic_launcher_foreground2, null);

                address = cursor.getString(cursor.getColumnIndex("address"));
                date = "志愿日期：" + cursor.getString(cursor.getColumnIndex("date"));
                content = "\u3000\u3000" + cursor.getString(cursor.getColumnIndex("content")).replaceAll("\n", "\n\u3000\u3000");
                phone = "志愿活动联系方式：" + cursor.getString(cursor.getColumnIndex("phone"));
                state = cursor.getInt(cursor.getColumnIndex("state"));
            }
        }
        imageView.setImageDrawable(image);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        toolbar.setTitle(title);
        addressView.setText(address);
        dateView.setText(date);
        contentView.setText(content);
        phoneView.setText(phone);
        if (state == 1) {
            button2.setText("取消报名");
            button2.setBackgroundResource(R.drawable.cancel_button);
        }

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AppControlor.isServerConnected) {
                    Intent intent = new Intent(VolDetailActivity.this, WebActivity.class);
                    intent.putExtra("url", "http://" + AppControlor.serverIp + "/map");
                    startActivity(intent);
                }else{
                    Toast.makeText(VolDetailActivity.this,"网络未连接，请检查网络设置",Toast.LENGTH_SHORT).show();
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppControlor.isServerConnected && position < AppControlor.volItemList.size()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                RequestBody requestBody = new FormBody.Builder()
                                        .add("name",position+1+"")
                                        .build();
                                Request request = new Request.Builder()
//                                .addHeader("content-type","application/json")
                                        .url("http://" + AppControlor.serverIp + "/change_state")
                                        .post(requestBody)
                                        .build();
                                client.newCall(request).execute();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    if (button2.getText().toString().equals("报名")) {
                        Toast.makeText(VolDetailActivity.this, "报名成功！", Toast.LENGTH_SHORT).show();
                        button2.setText("取消报名");
                        button2.setBackgroundResource(R.drawable.cancel_button);
                        volItem.setState("1");
                    }else {
                        Toast.makeText(VolDetailActivity.this, "已取消报名", Toast.LENGTH_SHORT).show();
                        button2.setText("报名");
                        button2.setBackgroundResource(R.drawable.orange_button);
                        volItem.setState("0");
                    }
                } else {
                    ContentValues values = new ContentValues();
                    if (button2.getText().toString().equals("报名")) {
                        Toast.makeText(VolDetailActivity.this, "报名成功！", Toast.LENGTH_SHORT).show();
                        button2.setText("取消报名");
                        button2.setBackgroundResource(R.drawable.cancel_button);
                        values.put("state", 1);
                    } else {
                        Toast.makeText(VolDetailActivity.this, "已取消报名", Toast.LENGTH_SHORT).show();
                        button2.setText("报名");
                        button2.setBackgroundResource(R.drawable.orange_button);
                        values.put("state", 0);
                    }
                    db.update("vol", values, "title=?", new String[]{title});
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        if (cursor != null) cursor.close();
        if (db != null) db.close();
        super.onDestroy();
    }
}