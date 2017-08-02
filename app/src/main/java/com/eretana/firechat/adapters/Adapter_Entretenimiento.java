package com.eretana.firechat.adapters;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eretana.firechat.Miniapp_lovefinder;
import com.eretana.firechat.R;
import com.eretana.firechat.models.Miniapp;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edgar on 27/7/2017.
 */

public class Adapter_Entretenimiento extends BaseAdapter implements View.OnClickListener{

    private Context context;
    private List<Miniapp> apps;

    public Adapter_Entretenimiento(Context context) {
        this.context = context;
        this.apps = new ArrayList<>();
        apps.add(new Miniapp("Love Finder",0,R.drawable.app_love_search));
        apps.add(new Miniapp("Foto del Mes",0,R.drawable.app_camera_contest));
        apps.add(new Miniapp("Comerciales",0,R.drawable.app_coinstv));
    }

    @Override
    public int getCount() {
        return apps.size();
    }

    @Override
    public Object getItem(int position) {
        return apps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final Miniapp app = apps.get(position);

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_miniapp,parent,false);
        }

        ImageView iv = (ImageView) view.findViewById(R.id.miniapp_img);
        TextView tv = (TextView) view.findViewById(R.id.miniapp_name);

        iv.setImageResource(app.getDrawable());
        tv.setText(app.getName());
        view.setTag(app.getIntent());
        view.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        int activity = Integer.parseInt(v.getTag().toString());
        Intent intent = null;

        switch (activity){
            case 0:
                intent = new Intent(context, Miniapp_lovefinder.class);
            break;
            case 1:

            break;
        }

        if(intent != null){
            context.startActivity(intent);
        }
    }
}
