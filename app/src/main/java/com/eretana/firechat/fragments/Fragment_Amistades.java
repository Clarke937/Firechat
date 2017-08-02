package com.eretana.firechat.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.eretana.firechat.R;
import com.eretana.firechat.adapters.Adapter_Friendlist;
import com.eretana.firechat.database.SQLFriends;
import com.eretana.firechat.database.DBFirebase;
import com.eretana.firechat.models.Friend;
import com.eretana.firechat.utils.Timestamp_utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edgar on 23/6/2017.
 */

public class Fragment_Amistades extends Fragment implements ValueEventListener{

    private ListView listview;
    private SQLFriends db;
    private List<Friend> friends;
    private Adapter_Friendlist adapter;
    private DatabaseReference conexionref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_amistades,container,false);
        init_controls(fragment);
        populate_listview();
        return fragment;
    }

    public void init_controls(View fragment){
        listview = (ListView) fragment.findViewById(R.id.friend_listview);
        db = new SQLFriends(getContext());
        friends = new ArrayList<>();
        adapter = new Adapter_Friendlist(friends,getContext());
        listview.setAdapter(adapter);

        conexionref = new DBFirebase().get_conexion();
        conexionref.addValueEventListener(this);
    }


    public void populate_listview(){
        Cursor c = db.select();
        friends.clear();

        while (c.moveToNext()){
            Friend friend = new Friend();
            friend.setUid(c.getString(0));
            friend.setUsername(c.getString(1));
            friend.setEmail(c.getString(2));
            friends.add(friend);
        }

        conexionref.addListenerForSingleValueEvent(this);
        adapter.notifyDataSetChanged();
    }

    //ON CONEXION CHANGE

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        if(!friends.isEmpty()){
            for(Friend f : friends){
                String uid = f.getUid();
                Log.e("UID: ",uid);

                String val = dataSnapshot.child(uid).child("online").getValue().toString();
                String lastime = dataSnapshot.child(uid).child("lastime").getValue().toString();

                boolean online = Boolean.parseBoolean(val);
                f.setOnline(online);

                if(!online){
                    Timestamp current = new Timestamp(System.currentTimeMillis());
                    Timestamp offline = new Timestamp(Long.parseLong(lastime));
                    long miliseconds = current.getTime() - offline.getTime();
                    f.setLastime(Timestamp_utils.elapsed_time(miliseconds));
                }

            }

            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
