package com.eretana.firechat.listeners;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.eretana.firechat.database.DBFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Edgar on 1/7/2017.
 */

public class Request_listener implements ValueEventListener {

    private Context context;
    private Map<String,String> data;
    private DatabaseReference ref;

    public Request_listener(Context c, Map<String,String> d){
        this.context = c;
        this.data = d;
    }

    public void check_and_send(){
        ref = new DBFirebase().get_requests().child(data.get("to_uid"));
        ref.addListenerForSingleValueEvent(this);
    }



    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        if(dataSnapshot.child(data.get("from_uid")).exists()){

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Accion Invalida");
            builder.setMessage("Ya has enviado una solicitud de amistad a este usuario");
            builder.setNegativeButton("Entendido",null);
            builder.create().show();

        }else{
            send();
        }

    }

    private void send(){

        Map<String,Object> mapdata = new TreeMap<>();

        String myuid = data.get("from_uid");
        String username = data.get("from_username");
        String email = data.get("email");
        String description = data.get("desc");
        String date = data.get("date");

        mapdata.put("username",username);
        mapdata.put("email",email);
        mapdata.put("description",description);
        mapdata.put("date",date);
        mapdata.put("watched",false);

        ref.child(myuid).setValue(mapdata);
    }


    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
