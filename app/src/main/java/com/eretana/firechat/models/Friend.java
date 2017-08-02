package com.eretana.firechat.models;

/**
 * Created by Edgar on 24/6/2017.
 */

public class Friend {

    private String email;
    private String username;
    private boolean online;
    private String uid;
    private String lastime;
    private int sex;

    public Friend() {
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLastime() {
        return lastime;
    }

    public void setLastime(String lastime) {
        this.lastime = lastime;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
