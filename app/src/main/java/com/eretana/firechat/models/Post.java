package com.eretana.firechat.models;

import android.graphics.Bitmap;

/**
 * Created by Edgar on 4/7/2017.
 */

public class Post {

    public enum TYPES {TEXT,IMAGE}

    private String uid;
    private String id;
    private String title;
    private long timestamp;
    private String text;
    private Bitmap image;
    private TYPES type;
    private String emoji;
    private int likes;
    private String imageurl;


    public Post() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public TYPES getType() {
        return type;
    }

    public void setType(TYPES type) {
        this.type = type;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
