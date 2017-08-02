package com.eretana.firechat.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.eretana.firechat.R;
import com.eretana.firechat.adapters.Adapter_Conversaciones;
import com.eretana.firechat.database.DBFirebase;
import com.eretana.firechat.models.Conversation;
import com.eretana.firechat.models.Session;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edgar on 23/6/2017.
 */

public class Fragment_Conversaciones extends Fragment implements ValueEventListener{

    private ListView listview;
    private DatabaseReference ref;
    private String myuid;
    private List<Conversation> conversaciones;
    private Adapter_Conversaciones adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_conversaciones, container, false);
        init_controls(fragment);
        return fragment;
    }

    public void init_controls(View v){
        listview = (ListView) v.findViewById(R.id.conversaciones_listview);
        myuid = Session.getUid();
        conversaciones = new ArrayList<>();
        adapter = new Adapter_Conversaciones(getContext(),conversaciones);
        listview.setAdapter(adapter);


        ref = new DBFirebase().get_chats();
        ref.addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        conversaciones.clear();

        for(DataSnapshot data : dataSnapshot.getChildren()){

            String uid01 = data.child("user01").child("uid").getValue().toString();
            String uid02 = data.child("user02").child("uid").getValue().toString();

            String friendname = "";

            if(uid01.equals(myuid) || uid02.equals(myuid)){

                boolean is01 = uid01.equals(myuid);
                Conversation con = new Conversation();

                if(is01){
                    friendname = data.child("user02").child("username").getValue().toString();
                    con.setUid(uid02);
                    con.setUsername(friendname);
                }else{
                    friendname = data.child("user01").child("username").getValue().toString();
                    con.setUid(uid01);
                    con.setUsername(friendname);
                }

                String message = data.child("last_message").child("text").getValue().toString();
                long time = Long.parseLong(data.child("last_message").child("timestamp").getValue().toString());

                con.setLast_message(message);
                con.setTimestamp(time);
                con.setChatkey(data.getKey());

                conversaciones.add(con);
            }
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
