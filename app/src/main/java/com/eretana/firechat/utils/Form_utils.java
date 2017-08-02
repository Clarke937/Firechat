package com.eretana.firechat.utils;

import android.widget.EditText;

/**
 * Created by Edgar on 22/6/2017.
 */

public final class Form_utils {

    public static String text(EditText et){
        return et.getText().toString();
    }

    public static boolean are_fill(EditText... et){
        for (EditText e: et) {
            if(e.getText().toString().length() == 0) {
                return false;
            }
        }
        return true;
    }

}
