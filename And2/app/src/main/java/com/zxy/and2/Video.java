package com.zxy.and2;


public class Video {
    private String title;
    private long length;
    private String imageUrl;
    private String videoUrl;
    private String author;
    private String content;

    public Video() {
    }

    public Video(String title, long length, String imageUrl, String videoUrl, String author, String content) {
        this.title = title;
        this.length = length;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.author = author;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

//
//public class Video {
//    private String title;
//    private String author;
//    private String content;
//    private long length;
//    private int videoUrl;
//
//    public Video() {
//    }
//
//    public Video(String title, String name, String content, long length, int videoUrl) {
//        this.title = title;
//        this.author = name;
//        this.content = content;
//        this.length = length;
//        this.videoUrl = videoUrl;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public long getLength() {
//        return length;
//    }
//
//    public void setLength(long length) {
//        this.length = length;
//    }
//
//    public int getVideoUrl() {
//        return videoUrl;
//    }
//
//    public void setVideoUrl(int videoUrl) {
//        this.videoUrl = videoUrl;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public String getAuthor() {
//        return author;
//    }
//
//    public void setAuthor(String author) {
//        this.author = author;
//    }
//}
