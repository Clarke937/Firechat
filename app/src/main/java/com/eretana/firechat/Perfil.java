package com.eretana.firechat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.eretana.firechat.adapters.Adapter_Perfil_Pager;
import com.eretana.firechat.database.DBStorage;
import com.eretana.firechat.database.SQLFriends;
import com.eretana.firechat.database.SQLUser;
import com.eretana.firechat.fragments.Fragment_Amistades;
import com.eretana.firechat.fragments.Fragment_Perfil;
import com.eretana.firechat.fragments.Fragment_Publicaciones;
import com.eretana.firechat.listeners.Chatroom_listener;
import com.eretana.firechat.listeners.Request_listener;
import com.eretana.firechat.models.Constants;
import com.eretana.firechat.models.Session;
import com.eretana.firechat.utils.Timestamp_utils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.util.Map;
import java.util.TreeMap;


public class Perfil extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tablayout;
    private ViewPager viewpager;
    private Adapter_Perfil_Pager adapter;
    private ImageView mainimage;
    private String profileuid;
    private Bitmap profilebitmap;
    private View view_cover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Perfil");
        init_controls();
    }

    public void init_controls(){
        profileuid = getIntent().getStringExtra("uid");
        mainimage = (ImageView) findViewById(R.id.perfil_mainimage);
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        view_cover = (View) findViewById(R.id.cover_profile_picture);

        adapter = new Adapter_Perfil_Pager(getSupportFragmentManager());
        tablayout.setupWithViewPager(viewpager);

        view_cover.setOnTouchListener(new PictureEvents());
        download_profile_image();
        setupViewPager();
    }

    public void setupViewPager(){
        Bundle bundle = new Bundle();
        bundle.putString("uid",profileuid);

        Fragment fg1 = new Fragment_Perfil();
        Fragment fg2 = new Fragment_Publicaciones();
        fg1.setArguments(bundle);
        fg2.setArguments(bundle);

        adapter.add_fragment("Perfil",fg1);
        adapter.add_fragment("Publicaciones",fg2);

        viewpager.setAdapter(adapter);
    }

    public void download_profile_image(){
        long SIZE = Constants.MEGABYTE * 2;
        StorageReference storage = new DBStorage().get_profile_picture(profileuid);

        storage.getBytes(SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                profilebitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length,null);
                mainimage.setImageBitmap(profilebitmap);
            }
        });
    }

    public void send_msj(View v) {
        SQLFriends friends = new SQLFriends(this);
        boolean isFriend = friends.exist(profileuid);

        if(!isFriend){
            Snackbar.make(v,"Este usuario no es tu amigo",Snackbar.LENGTH_LONG).show();
        }else {
            new Chatroom_listener(this,profileuid);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean flag = false;

        if(profileuid.equals(Session.getUid())){
            getMenuInflater().inflate(R.menu.menu_my_profile,menu);
            flag = true;
        }else{
            SQLFriends friends = new SQLFriends(this);
            if(!friends.exist(profileuid)){
                getMenuInflater().inflate(R.menu.menu_profile_visit,menu);
                flag = true;
            }
        }

        return flag;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_edit_profile:
                Intent intent = new Intent(this,Edit_profile.class);
                intent.putExtra("mode","edit");
                intent.putExtra("uid",profileuid);
                startActivity(intent);
                break;

            case R.id.action_add_friend:
                friend_request();
            break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void friend_request(){

        SQLUser user = new SQLUser(this);

        String myuid = Session.getUid();
        String date = Timestamp_utils.current_date();
        String username = user.get_username();
        String email = user.get_email();
        String description = user.get_description();

        Map<String,String> data = new TreeMap<>();
        data.put("to_uid",profileuid);
        data.put("from_uid",myuid);
        data.put("from_username",username);
        data.put("date",date);
        data.put("email",email);
        data.put("desc",description);

        new Request_listener(this,data).check_and_send();
    }


    public class PictureEvents implements View.OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) {

           switch (event.getAction()){
               case MotionEvent.ACTION_DOWN:
                   v.setAlpha(0);
               break;
               case MotionEvent.ACTION_UP:
                   v.setAlpha(0.3f);
               break;

           }

           return true;
        }
    }

}
