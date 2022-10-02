package com.zxy.and2.placeholder;

import android.content.Context;
import android.content.SharedPreferences;

import com.zxy.and2.AppControlor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DakaPlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    public static final List<DakaItem> ITEMS = new ArrayList<DakaItem>();

    /**
     * A map of sample (placeholder) items, by ID.
     */
    public static final Map<String, DakaItem> ITEM_MAP = new HashMap<String, DakaItem>();

    private static final int COUNT = 5;

    public static void init(){
        // Add some sample items.
//        for (int i = 0; i < COUNT; i++) {
//            addItem(createPlaceholderItem(1+4*i,R.drawable.person1,"王芊娅", "5月15日"));
//            addItem(createPlaceholderItem(2+4*i,R.drawable.person2,"宋博名", "5月14日"));
//            addItem(createPlaceholderItem(3+4*i,R.drawable.person3,"李佳宇", "5月13日"));
//            addItem(createPlaceholderItem(4+4*i,R.drawable.person4,"赵嘉霖", "5月12日"));
//            addItem(createPlaceholderItem(5+4*i,R.drawable.person5,"杨知旋", "5月11日"));
//            addItem(createPlaceholderItem(6+4*i,R.drawable.person6,"孟临", "5月10日"));
//        }
        ITEMS.clear();
        SharedPreferences pref= AppControlor.mainInstance.getSharedPreferences(AppControlor.id+"_photo", Context.MODE_PRIVATE);
        int sum= pref.getInt("sum",0);
        for(int i=sum;i>=1;i--) {
            addItem(createPlaceholderItem(i, pref.getString("image"+i,null), pref.getString("content"+i,null), pref.getString("date"+i,null), pref.getString("cache"+i,null)));
        }
    }

    public static void addItem(DakaItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static DakaItem createPlaceholderItem(int position, String imagePath, String content, String date, String cachePath) {
        return new DakaItem(String.valueOf(position), imagePath, cachePath, content, date);
    }


    /**
     * A placeholder item representing a piece of content.
     */
    public static class DakaItem {
        public final String id;
        public final String imagePath;
        public final String cachePath;
        public final String content;
        public final String date;

        public DakaItem(String id, String imagePath, String cachePath, String content, String date) {
            this.id = id;
            this.imagePath = imagePath;
            this.cachePath = cachePath;
            this.content = content;
            this.date = date;
        }

        @Override
        public String toString() {
            return id;
        }
    }
}