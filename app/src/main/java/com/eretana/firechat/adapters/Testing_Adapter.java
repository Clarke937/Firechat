package com.eretana.firechat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eretana.firechat.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Edgar on 7/7/2017.
 */

public class Testing_Adapter extends BaseAdapter {

    private List<String> textos;
    private Context context;

    public Testing_Adapter(Context c) {
        this.textos = new ArrayList<>();
        this.context = c;
        for(int i = 0; i < 50; i++){
            this.textos.add("Este es el texto numero " + i);
        }
    }

    @Override
    public int getCount() {
        return textos.size();
    }

    @Override
    public Object getItem(int position) {
        return textos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_textview,parent,false);
        }

        TextView tv = (TextView) view.findViewById(R.id.test_textview);
        tv.setText(textos.get(position));
        return view;
    }
}
