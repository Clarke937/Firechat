package com.eretana.firechat.listeners;

import android.content.Context;
import android.database.Cursor;

import com.eretana.firechat.database.SQLFriends;
import com.eretana.firechat.database.SQLikes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Edgar on 29/6/2017.
 */

public class Hotlikes_listener implements ValueEventListener{

    private SQLikes db;

    public Hotlikes_listener(Context c) {
        this.db = new SQLikes(c);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        for (DataSnapshot data : dataSnapshot.getChildren()){
            String uid = data.getKey();
            if(!db.get_status(uid)){
                db.like(uid);
            }
        }

        Cursor c = db.select();
        while (c.moveToNext()){
            String _uid = c.getString(0);
            if(!dataSnapshot.child(_uid).exists()){
                db.dislike(_uid);
            }
        }

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
