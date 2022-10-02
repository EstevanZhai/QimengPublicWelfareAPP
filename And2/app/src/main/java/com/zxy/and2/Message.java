package com.zxy.and2;

public class Message {
    public static int TYPE_RECEIVED = 1;
    public static int TYPE_SENT = 0;

    private String content;

    private int type;

    public Message(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public static void init(){
        TYPE_RECEIVED = AppControlor.USER_TYPE.equals("1") ? 0 : 1;
        TYPE_SENT = AppControlor.USER_TYPE.equals("1") ? 1 : 0;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }
}
