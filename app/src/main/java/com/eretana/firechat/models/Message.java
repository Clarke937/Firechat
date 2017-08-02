package com.eretana.firechat.models;

/**
 * Created by Edgar on 25/7/2017.
 */

public class Message {

    public String uid;
    public String date;
    public String text;
    public long timestamp;

    public Message() {
    }

    public Message(String uid, String date, String text, long timestamp) {
        this.uid = uid;
        this.date = date;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
