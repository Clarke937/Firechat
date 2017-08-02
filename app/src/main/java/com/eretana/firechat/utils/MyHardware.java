package com.eretana.firechat.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.*;
import android.os.Environment;

/**
 * Created by Edgar on 16/7/2017.
 */

public class MyHardware {

    private Context context;

    public MyHardware(Context context) {
        this.context = context;
    }

    public boolean existCamera(){
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static boolean existMicroSD(){
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }


}
