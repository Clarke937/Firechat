package com.eretana.firechat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eretana.firechat.R;

import java.util.List;

/**
 * Created by Edgar on 26/6/2017.
 */

public class Adapter_Profile extends BaseAdapter{

    private List<String> lista;
    private Context context;

    public Adapter_Profile(Context c, List<String> lista) {
        this.lista = lista;
        this.context = c;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
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
        tv.setText(lista.get(position));
        return view;
    }
}
