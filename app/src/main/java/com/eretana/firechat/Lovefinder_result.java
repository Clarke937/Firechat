package com.eretana.firechat;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.GridView;

import com.eretana.firechat.adapters.Adapter_Lovefinder;
import com.eretana.firechat.database.DBStorage;
import com.eretana.firechat.models.Constants;
import com.eretana.firechat.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Lovefinder_result extends AppCompatActivity{

    private Toolbar toolbar;
    private ArrayList<User> users;
    private GridView grid;
    private Adapter_Lovefinder adapter;
    private StorageReference storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lovefinder_result);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init_controls();
    }

    private void init_controls(){

        users = (ArrayList<User>) getIntent().getSerializableExtra("users");
        grid = (GridView) findViewById(R.id.gridloveresults);
        adapter = new Adapter_Lovefinder(this,users);

        this.setTitle("Resultados (" + users.size() + ")");

        for(final User user : users) {
            String uid = user.getUid();
            Log.e("LFR",uid);
            storage = new DBStorage().get_profile_picture(uid);
            storage.getBytes(Constants.MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length,null);
                    if(bitmap != null){
                        user.setImage(bitmap);
                        Log.e("LFR","NOT NULL");
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }

        grid.setAdapter(adapter);
    }


}
