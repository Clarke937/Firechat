package com.eretana.firechat.utils;

import android.widget.LinearLayout;

/**
 * Created by Edgar on 11/7/2017.
 */

public final class LayoutParams_utils {

    private static final int match = LinearLayout.LayoutParams.MATCH_PARENT;
    private static final int wrap = LinearLayout.LayoutParams.WRAP_CONTENT;

    public static LinearLayout.LayoutParams setMargins(int l,int t,int r, int b){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(match,wrap);
        params.setMargins(l,t,r,b);
        return params;
    }

}
