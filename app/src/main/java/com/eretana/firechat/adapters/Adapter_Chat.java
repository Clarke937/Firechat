package com.eretana.firechat.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eretana.firechat.R;
import com.eretana.firechat.models.Message;
import com.eretana.firechat.models.Session;
import com.eretana.firechat.utils.Timestamp_utils;

import java.util.List;

/**
 * Created by Edgar on 25/7/2017.
 */

public class Adapter_Chat extends BaseAdapter {

    public Context context;
    public List<Message> mensajes;

    public Adapter_Chat(Context context, List<Message> mensajes) {
        this.context = context;
        this.mensajes = mensajes;
    }

    @Override
    public int getCount() {
        return mensajes.size();
    }

    @Override
    public Object getItem(int position) {
        return mensajes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_chatbubble,parent,false);
        }

        Message msj = mensajes.get(position);

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.chatroom_bubble);
        LinearLayout container = (LinearLayout) view.findViewById(R.id.layout_chatbubble);
        TextView tvmessage = (TextView) view.findViewById(R.id.chatroom_message);
        TextView tvtimer = (TextView) view.findViewById(R.id.chatroom_timer);


        boolean isMine = msj.getUid().equals(Session.getUid());

        if(isMine){
            layout.setBackgroundResource(R.drawable.bubble2_right);
            container.setGravity(Gravity.RIGHT);
        }else{
            layout.setBackgroundResource(R.drawable.bubble2_left);
            container.setGravity(Gravity.LEFT);
        }


        long miliseconds = Timestamp_utils.current_time() - msj.getTimestamp();
        String elapsed = "Enviado hace: " + Timestamp_utils.elapsed_time(miliseconds);
        tvmessage.setText(msj.getText());
        tvtimer.setText(elapsed);

        return view;
    }

    public void add(Message msj){
        mensajes.add(msj);
        this.notifyDataSetChanged();
    }

    public void addTemporal(String msj){
        Message m = new Message(Session.getUid(),"",msj,0);
        mensajes.add(m);
        this.notifyDataSetChanged();
    }

    public void clear(){
        mensajes.clear();
        this.notifyDataSetChanged();
    }




}
