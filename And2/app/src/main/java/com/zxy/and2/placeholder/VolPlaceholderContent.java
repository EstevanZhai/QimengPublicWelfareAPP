package com.zxy.and2.placeholder;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.zxy.and2.AppControlor;
import com.zxy.and2.MainActivity;
import com.zxy.and2.R;
import com.zxy.and2.VolItem;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class VolPlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    public static final List<VolunteeringItem> ITEMS = new ArrayList<VolunteeringItem>();

    /**
     * A map of sample (placeholder) items, by ID.
     */
    public static final Map<String, VolunteeringItem> ITEM_MAP = new HashMap<String, VolunteeringItem>();

    private static final int COUNT = 18;

    private static final Cursor cursor;

    static {
        // Add some sample items.
        cursor = MainActivity.cursor;
        cursor.moveToFirst();
        for (int i = 1; i <= COUNT; i++) {
            addItem(createPlaceholderItem(i));
        }
        cursor.close();
    }

    private static void addItem(VolunteeringItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static VolunteeringItem createPlaceholderItem(int position) {
        if (AppControlor.isServerConnected&&AppControlor.volItemList!=null&&position<=AppControlor.volItemList.size()) {
            VolItem volItem=AppControlor.volItemList.get(position-1);
            return new VolunteeringItem(String.valueOf(position), volItem.getImage(), volItem.getTitle(), volItem.getSubtitle());
        } else {
            if (!cursor.isAfterLast()) {
                @SuppressLint("Range") String stitle = cursor.getString(cursor.getColumnIndex("title"));
                String title = (stitle != null) ? stitle : ("留守儿童公益活动 " + position);

                @SuppressLint("Range") String imageString = cursor.getString(cursor.getColumnIndex("image"));
                Drawable image;
                if (imageString != null) {
                    byte[] imageCode = Base64.decode(imageString, Base64.DEFAULT);
                    Bitmap bmpout = BitmapFactory.decodeByteArray(imageCode, 0, imageCode.length);
                    image = new BitmapDrawable(Resources.getSystem(), bmpout);
                } else
                    image = ResourcesCompat.getDrawable(AppControlor.mainInstance.getResources(), R.drawable.ic_launcher_foreground2, null);

                @SuppressLint("Range") String scontent = cursor.getString(cursor.getColumnIndex("content"));
                String content;
                if (scontent != null && scontent.length() > 39)
                    content = scontent.substring(0, 39) + "...";
                else if (scontent != null) content = scontent;
                else content = "点击查看详情";
//            if(!cursor.isLast())
                cursor.moveToNext();
                return new VolunteeringItem(String.valueOf(position), image, title, content);
            } else
                return new VolunteeringItem(String.valueOf(position), ResourcesCompat.getDrawable(AppControlor.mainInstance.getResources(), R.drawable.ic_launcher_foreground3, null), "留守儿童公益活动 " + position, "details");
        }
    }


    /**
     * A placeholder item representing a piece of content.
     */
    public static class VolunteeringItem {
        public final String id;
        public final Drawable image;
        public final String title;
        public final String content;

        public VolunteeringItem(String id, Drawable image, String title, String content) {
            this.id = id;
            this.image = image;
            this.title = title;
            this.content = content;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}