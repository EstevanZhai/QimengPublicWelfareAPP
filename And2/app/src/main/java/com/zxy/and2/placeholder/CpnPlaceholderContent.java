package com.zxy.and2.placeholder;

import com.zxy.and2.AppControlor;
import com.zxy.and2.R;

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
public class CpnPlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    public static final List<CompanionItem> ITEMS = new ArrayList<CompanionItem>();

    /**
     * A map of sample (placeholder) items, by ID.
     */
    public static final Map<String, CompanionItem> ITEM_MAP = new HashMap<String, CompanionItem>();

    private static final int COUNT = 5;

    public static void init() {
        // Add some sample items.
        ITEMS.clear();
        if (AppControlor.USER_TYPE.equals("1")) {
            for (int i = 0; i < COUNT; i++) {
                addItem(createPlaceholderItem(1 + 4 * i, R.drawable.person1, "王芊娅"));
                addItem(createPlaceholderItem(2 + 4 * i, R.drawable.person2, "宋博名"));
                addItem(createPlaceholderItem(3 + 4 * i, R.drawable.person3, "李佳宇"));
                addItem(createPlaceholderItem(4 + 4 * i, R.drawable.person4, "赵嘉霖"));
                addItem(createPlaceholderItem(5 + 4 * i, R.drawable.person5, "杨知旋"));
                addItem(createPlaceholderItem(6 + 4 * i, R.drawable.person6, "孟临"));
            }
        } else {
            for (int i = 0; i < COUNT*5; i++) {
                addItem(createPlaceholderItem(i, R.drawable.panzhoudan, "潘周聃"));
            }
        }
    }

    private static void addItem(CompanionItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static CompanionItem createPlaceholderItem(int position, int imageId, String name) {
        return new CompanionItem(String.valueOf(position), imageId, name);
    }


    /**
     * A placeholder item representing a piece of content.
     */
    public static class CompanionItem {
        public final String id;
        public final int imageId;
        public final String name;

        public CompanionItem(String id, int imageId, String name) {
            this.id = id;
            this.imageId = imageId;
            this.name = name;
        }

        @Override
        public String toString() {
            return id;
        }
    }
}