package com.eretana.firechat.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.eretana.firechat.R;

/**
 * Created by Edgar on 9/7/2017.
 */

public class Alert_utils {

    private AlertDialog.Builder dialog;
    private Context context;

    public Alert_utils(Context c) {
        this.dialog = new AlertDialog.Builder(c);
        this.context = c;
    }

    public void show_warning_alert(String text){

        View view = LayoutInflater.from(context).inflate(R.layout.vista_warning_alert,null);
        TextView tv = (TextView) view.findViewById(R.id.warning_text);
        tv.setText(text);

        dialog.setView(view);
        dialog.setPositiveButton("Ok",null);
        dialog.create().show();
    }




}
