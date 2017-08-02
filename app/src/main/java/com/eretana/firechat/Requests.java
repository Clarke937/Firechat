package com.eretana.firechat;

import android.animation.StateListAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;

import com.eretana.firechat.adapters.Adapter_Requests;
import com.eretana.firechat.database.DBFirebase;
import com.eretana.firechat.models.Request;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Requests extends AppCompatActivity implements ValueEventListener{

    private ListView listview;
    private List<Request> solicitudes;
    private Query ref;
    private Adapter_Requests adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        this.setTitle("Solicitudes de Amistad");
        init_controls();
    }

    public void init_controls(){
        listview = (ListView) findViewById(R.id.requests_list);
        solicitudes = new ArrayList<>();
        adapter = new Adapter_Requests(this,solicitudes);
        ref = new DBFirebase().get_my_requests();

        //Setteres
        ref.addValueEventListener(this);
        listview.setAdapter(adapter);


    }


    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        solicitudes.clear();

        for(DataSnapshot data : dataSnapshot.getChildren()){
            Request request = new Request();
            request.setUid(data.getKey());
            request.setUsername(data.child("username").getValue().toString());
            request.setEmail(data.child("email").getValue().toString());
            request.setDescription(data.child("description").getValue().toString());
            request.setDate(data.child("date").getValue().toString());
            request.setWatched(Boolean.parseBoolean(data.child("watched").getValue().toString()));

            solicitudes.add(request);
        }

        this.setTitle("Solicitudes de Amistad (" + solicitudes.size() + ")");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onBackPressed() {
        ref.removeEventListener(this);
        super.onBackPressed();
    }



}
