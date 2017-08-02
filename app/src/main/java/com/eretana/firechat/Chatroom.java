package com.eretana.firechat;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.eretana.firechat.adapters.Adapter_Chat;
import com.eretana.firechat.database.DBFirebase;
import com.eretana.firechat.models.Message;
import com.eretana.firechat.models.Session;
import com.eretana.firechat.utils.Timestamp_utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class Chatroom extends AppCompatActivity implements ValueEventListener, View.OnClickListener{

    private Toolbar toolbar;
    private List<Message> mensajes;
    private ListView listview;
    private Adapter_Chat adapter;
    private String chatroomkey;
    private Query ref;
    private EditText inputmsj;
    private FloatingActionButton fabsend;
    private DatabaseReference conexion,writing;
    private ValueEventListener listenerconexion,writinglistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init_controls();
    }

    private void init_controls(){

        mensajes = new ArrayList<>();
        listview = (ListView) findViewById(R.id.chatroom_listview);
        adapter = new Adapter_Chat(this,mensajes);
        chatroomkey = getIntent().getStringExtra("chatkey");
        ref = new DBFirebase().get_chat(chatroomkey);
        writing = ref.getRef().getParent().child("writing");
        listenerconexion = new FriendConexion();
        writinglistener = new WritingListener();
        conexion = new DBFirebase().get_userConexion(getIntent().getStringExtra("frienduid"));
        inputmsj = (EditText) findViewById(R.id.chatroom_input);
        fabsend = (FloatingActionButton) findViewById(R.id.chatroom_fab);
        fabsend.setOnClickListener(this);

        //Setters
        listview.setAdapter(adapter);
        ref.addValueEventListener(this);
        conexion.addValueEventListener(listenerconexion);
        writing.addValueEventListener(writinglistener);
        inputmsj.addTextChangedListener(new UserTyping());
        this.setTitle(getIntent().getStringExtra("friendname"));

    }


    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        adapter.clear();

        for (DataSnapshot data : dataSnapshot.getChildren()){

            Message msj = new Message();

            String uid = data.child("by").getValue().toString();
            String date = data.child("date").getValue().toString();
            String text = data.child("text").getValue().toString();
            long time = Long.parseLong(data.child("timestamp").getValue().toString());

            msj.setUid(uid);
            msj.setDate(date);
            msj.setText(text);
            msj.setTimestamp(time);

            adapter.add(msj);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onClick(View v) {
        String msj = inputmsj.getText().toString();
        long time = Timestamp_utils.current_time();
        String date = Timestamp_utils.current_date();

        Message m = new Message(Session.getUid(),date,msj,time);

        Map<String,Object> mapa = new TreeMap<>();
        mapa.put("by",m.getUid());
        mapa.put("date",m.getDate());
        mapa.put("text",m.getText());
        mapa.put("timestamp",m.getTimestamp());

        //SEND MESSAGE
        inputmsj.setText("");
        if(msj.length() > 0){
            new DBFirebase().send_message(chatroomkey,mapa);
            adapter.add(m);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chatroom,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                inputmsj.setText("");
                conexion.removeEventListener(listenerconexion);
                ref.removeEventListener(this);
                writing.removeEventListener(writinglistener);
                break;

            case R.id.action_upload_file:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private class FriendConexion implements ValueEventListener{

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            long lastime = Long.parseLong(dataSnapshot.child("lastime").getValue().toString());
            boolean online = Boolean.parseBoolean(dataSnapshot.child("online").getValue().toString());

            long time = Timestamp_utils.current_time() - lastime;
            String elapsed = Timestamp_utils.elapsed_time(time);

            if(online){
                toolbar.setSubtitle("En linea");
            }else{
                toolbar.setSubtitle("Desconectado/a hace: " + elapsed);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    private class UserTyping implements TextWatcher{

        private DatabaseReference dr;

        public UserTyping() {
            dr = ref.getRef().getParent();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Map<String,Object> data = new TreeMap<>();
            data.put("uid",Session.getUid());
            data.put("typing",true);
            dr.child("writing").setValue(data);
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.length() == 0){
                dr.child("writing").child("typing").setValue(false);
            }
        }
    }

    private class WritingListener implements ValueEventListener{

        @Override
        public void onDataChange(DataSnapshot data) {

            String uid = data.child("uid").getValue().toString();
            boolean typing = Boolean.parseBoolean(data.child("typing").getValue().toString());

            if(!uid.equals(Session.getUid()) && typing){
                toolbar.setSubtitle("Escribiendo...");
            }else{
                conexion.addListenerForSingleValueEvent(new FriendConexion());
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

}
