package com.eretana.firechat.models;

/**
 * Created by Edgar on 27/7/2017.
 */

public class Miniapp {

    private String name;
    private int intent;
    private int drawable;

    public Miniapp(String name, int intent, int drawable) {
        this.name = name;
        this.intent = intent;
        this.drawable = drawable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIntent() {
        return intent;
    }

    public void setIntent(int intent) {
        this.intent = intent;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }
}
