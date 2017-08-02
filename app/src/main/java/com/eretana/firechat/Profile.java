package com.eretana.firechat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eretana.firechat.adapters.Adapter_Post;
import com.eretana.firechat.database.DBStorage;
import com.eretana.firechat.database.SQLFriends;
import com.eretana.firechat.database.SQLUser;
import com.eretana.firechat.database.SQLikes;
import com.eretana.firechat.listeners.Post_listener;
import com.eretana.firechat.listeners.Request_listener;
import com.eretana.firechat.database.DBFirebase;
import com.eretana.firechat.models.Constants;
import com.eretana.firechat.models.Post;
import com.eretana.firechat.models.Session;
import com.eretana.firechat.utils.MyHardware;
import com.eretana.firechat.utils.MyRandoms;
import com.eretana.firechat.utils.Timestamp_utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Profile extends AppCompatActivity implements ValueEventListener{

    private String profile_uid;
    private Toolbar toolbar;
    private ListView postlisview;
    private ImageView profileimg;
    private TextView tvusername, tvdescription;
    private DatabaseReference profileref;
    private StorageReference storage;
    private ValueEventListener listener;
    private Adapter_Post adapter;
    private List<Post> publicaciones;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        profile_uid = getIntent().getStringExtra("uid");
        this.setTitle("Perfil de Usuario");
        init_controls();
    }

    private void init_controls(){
        profileref = new DBFirebase().get_user(profile_uid);
        storage = new DBStorage().get_profile_picture(profile_uid);
        listener = this;

        publicaciones = new ArrayList<>();
        postlisview = (ListView) findViewById(R.id.post_listview);
        tvusername = (TextView) findViewById(R.id.profile_username);
        tvdescription = (TextView) findViewById(R.id.profile_description);
        profileimg = (ImageView) findViewById(R.id.profile_bgpicture);

        View header = LayoutInflater.from(this).inflate(R.layout.header_postlist,null,false);

        //Setters
        //postlisview.addHeaderView(header,null,true);
        profileref.addListenerForSingleValueEvent(listener);
        download_profilepic();
    }

    private void download_profilepic(){
        long SIZE = Constants.MEGABYTE * 2;
        storage.getBytes(SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length,null);
                profileimg.setImageBitmap(bitmap);
                populate_listview(bitmap);
            }
        });
    }

    public void populate_listview(Bitmap b){
        adapter = new Adapter_Post(publicaciones,this,b);
        postlisview.setAdapter(adapter);

        Query qry = new DBFirebase().get_user_posts(profile_uid);
        qry.addValueEventListener(new Post_listener(adapter,publicaciones));
    }


    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        String username = dataSnapshot.child("username").getValue().toString();
        String description = dataSnapshot.child("description").getValue().toString();
        tvusername.setText(username);
        tvdescription.setText(description);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }




}
