package com.eretana.firechat.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Edgar on 10/7/2017.
 */

public class Conexion_utils {

    private ConnectivityManager cm;
    private NetworkInfo network;
    private Context context;

    public Conexion_utils(Context c){
        this.context = c;
        this.cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        this.network = cm.getActiveNetworkInfo();
    }

    public boolean isConnected(){
        return network != null && network.isConnected();
    }



}
