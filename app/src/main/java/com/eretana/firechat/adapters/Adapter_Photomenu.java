package com.eretana.firechat.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.eretana.firechat.R;

/**
 * Created by Edgar on 15/7/2017.
 */

public class Adapter_Photomenu extends ArrayAdapter<String> {

    private Integer[] iconos;
    private Context context;

    public Adapter_Photomenu(Context c, String[] opciones, Integer[] icons){
        super(c,R.layout.vista_photo_menu,opciones);
        this.iconos = icons;
        this.context = c;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView tv1 = (TextView) view.findViewById(R.id.text1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            tv1.setCompoundDrawablesRelativeWithIntrinsicBounds(iconos[position], 0, 0, 0);
        } else {
            tv1.setCompoundDrawablesWithIntrinsicBounds(iconos[position],0,0,0);
        }
        return view;
    }
}
