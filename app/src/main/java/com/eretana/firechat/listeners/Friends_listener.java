package com.eretana.firechat.listeners;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.eretana.firechat.database.SQLFriends;
import com.eretana.firechat.models.Friend;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Edgar on 26/6/2017.
 */

public class Friends_listener implements ValueEventListener {

    private SQLFriends db;
    private final int version = 1;

    public Friends_listener(Context c){
        this.db = new SQLFriends(c);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        for (DataSnapshot data : dataSnapshot.getChildren()){

            Friend friend = new Friend();
            friend.setUid(data.getKey());
            friend.setUsername(data.child("username").getValue().toString());
            friend.setEmail(data.child("email").getValue().toString());

            if(db.exist(friend.getUid())){
                db.update(friend);
            }else{
                db.insert(friend);
            }

        }

        Cursor c = db.select();
        while (c.moveToNext()){
            String uid = c.getString(0);
            if(!dataSnapshot.child(uid).exists()){
                db.delete(uid);
            }
        }

        db.showConsole();

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }



}
