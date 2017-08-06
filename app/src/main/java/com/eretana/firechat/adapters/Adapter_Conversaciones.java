package com.eretana.firechat.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eretana.firechat.Chatroom;
import com.eretana.firechat.R;
import com.eretana.firechat.models.Conversation;
import com.eretana.firechat.utils.Timestamp_utils;

import java.util.List;


public class Adapter_Conversaciones extends BaseAdapter implements View.OnClickListener{

    private Context context;
    private List<Conversation> conversaciones;

    public Adapter_Conversaciones(Context context, List<Conversation> conversaciones) {
        this.context = context;
        this.conversaciones = conversaciones;
    }

    @Override
    public int getCount() {
        return conversaciones.size();
    }

    @Override
    public Object getItem(int position) {
        return conversaciones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_conversacion,parent,false);
        }

        Conversation con = conversaciones.get(position);

        TextView tvusername = (TextView) convertView.findViewById(R.id.item_chat_username);
        TextView tvmessage = (TextView) convertView.findViewById(R.id.item_chat_last_msj);
        TextView tvtimer = (TextView) convertView.findViewById(R.id.item_chat_timer);

        long time = Timestamp_utils.current_time() - con.getTimestamp();
        String elapsed = Timestamp_utils.elapsed_time(time);

        tvusername.setText(con.getUsername());
        tvmessage.setText(con.getLast_message());
        tvtimer.setText(elapsed);

        convertView.setTag(con);
        convertView.setOnClickListener(this);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        Conversation con = (Conversation) v.getTag();
        Intent intent = new Intent(context, Chatroom.class);
        intent.putExtra("chatkey",con.getChatkey());
        intent.putExtra("friendname",con.getUsername());
        intent.putExtra("frienduid",con.getUid());
        context.startActivity(intent);
    }
}
