package com.eretana.firechat.utils;

import android.util.Log;

/**
 * Created by Edgar on 21/6/2017.
 */

public final class Thread_utils {

    public static void sleep(int time) {
        try {
            Thread.sleep(time);
        }catch (InterruptedException e){
            Log.e("Firechat.Thread_utils",e.getMessage());
        }
    }

}
