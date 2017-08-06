package com.eretana.firechat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eretana.firechat.R;

import com.eretana.firechat.adapters.Adapter_Comments;
import com.eretana.firechat.adapters.Testing_Adapter;
import com.eretana.firechat.database.DBFirebase;
import com.eretana.firechat.models.Comment;
import com.eretana.firechat.utils.MyDialogs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Post_viewer extends AppCompatActivity implements ValueEventListener{

    //Controls
    private FloatingActionButton fab;
    private ListView listview;
    private BottomSheetDialog bottomdialog;
    //POSTCARD
    private TextView tv_title;
    private TextView tv_text;
    //Data
    private String postid;
    private String uid;
    private List<Comment> comments;
    //Objects
    private DatabaseReference postref;
    private Adapter_Comments adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Publicacion");
        init_controls();
    }

    public void init_controls(){
        fab = (FloatingActionButton) findViewById(R.id.fab);
        postid = getIntent().getStringExtra("postid");
        uid = getIntent().getStringExtra("uid");
        bottomdialog = new MyDialogs().CommentDialog(this,uid,postid);
        comments = new ArrayList<>();
        postref = new DBFirebase().get_post(uid,postid);
        listview = (ListView) findViewById(R.id.post_comments);
        adapter = new Adapter_Comments(this,comments);
        listview.setAdapter(adapter);

        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.vista_post_header,listview,false);
        tv_title = (TextView) header.findViewById(R.id.postcard_title);
        tv_text = (TextView) header.findViewById(R.id.postcard_text);

        listview.addHeaderView(header);
        //Set Events
        postref.addValueEventListener(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this,Perfil.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
            break;
        }
        return true;
    }



    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        String title = dataSnapshot.child("title").getValue().toString();
        String text = dataSnapshot.child("text").getValue().toString();

        tv_title.setText(title);
        tv_text.setText(text);

        if(dataSnapshot.child("comments").exists()){
            comments.clear();

            for (DataSnapshot data : dataSnapshot.child("comments").getChildren()){

                Comment comment = new Comment();
                comment.setComment_id(data.getKey());
                comment.setUserid(data.child("by").getValue().toString());
                comment.setDate(data.child("date").getValue().toString());
                comment.setText(data.child("text").getValue().toString());
                comment.setTimestamp(Long.parseLong(data.child("timestamp").getValue().toString()));
                comment.setUsername(data.child("username").getValue().toString());
                comments.add(comment);
            }

            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public void newComment(View v){
        bottomdialog.show();
    }



}
