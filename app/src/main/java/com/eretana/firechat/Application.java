package com.eretana.firechat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.eretana.firechat.database.DBStorage;
import com.eretana.firechat.fragments.Fragment_Amistades;
import com.eretana.firechat.fragments.Fragment_Conversaciones;
import com.eretana.firechat.fragments.Fragment_Entretenimiento;
import com.eretana.firechat.listeners.Friends_listener;
import com.eretana.firechat.listeners.Search_listener;
import com.eretana.firechat.database.DBFirebase;
import com.eretana.firechat.models.Constants;
import com.eretana.firechat.models.Session;
import com.eretana.firechat.utils.Timestamp_utils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

public class Application extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //Navigation Controls
    private Switch conexion_switch;
    private TextView tv_username,tv_email,tv_requests_counter;

    //Activity Controls
    private DrawerLayout drawer;
    private NavigationView navigation;
    private FloatingActionButton fab_search;

    private DatabaseReference useref;
    private DatabaseReference friendsref;
    private Query requestref;
    private FirebaseUser user;
    private CircularImageView imgprofile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigation = (NavigationView) findViewById(R.id.nav_view);
        navigation.setNavigationItemSelectedListener(this);
        navigation.setCheckedItem(R.id.nav_chat);
        change_fragment(new Fragment_Conversaciones());

        init_controls();
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = Session.fb_auth.getCurrentUser();
        friendsref = new DBFirebase().get_friendlist(user.getUid());
        friendsref.addValueEventListener(new Friends_listener(this));
    }

    @Override
    protected void onStop() {
        super.onStop();
        friendsref.removeEventListener(new Friends_listener(this));
    }

    public void init_controls(){

        //GLOBAL VARS
        useref = new DBFirebase().get_myuser();
        requestref = new DBFirebase().get_my_requests();

        //Activity Controls
        fab_search = (FloatingActionButton) findViewById(R.id.fab);

        //Navigation Controls
        View nav_header = navigation.getHeaderView(0);
        conexion_switch = (Switch) nav_header.findViewById(R.id.header_switch);
        tv_username = (TextView) nav_header.findViewById(R.id.header_username);
        tv_email = (TextView) nav_header.findViewById(R.id.header_email);
        tv_requests_counter = (TextView) nav_header.findViewById(R.id.header_requests_counter);
        imgprofile = (CircularImageView) nav_header.findViewById(R.id.header_photo);

        online(true);

        //Listners
        conexion_switch.setOnCheckedChangeListener(new Conexion_Status());
        fab_search.setOnClickListener(new Search_listener());
        useref.addListenerForSingleValueEvent(new UserInfo());
        requestref.addValueEventListener(new RequestListener());
        download_profileImage();
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.application, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Fragment fragment = null;

        switch (id){
            case R.id.nav_signout:
                Session.fb_auth.signOut();
                startActivity(new Intent(this,Authentication.class));
                this.finish();
            break;

            case R.id.nav_chat:
                fragment = new Fragment_Conversaciones();
                this.setTitle("Conversaciones");
            break;

            case R.id.nav_friends:
                fragment = new Fragment_Amistades();
                this.setTitle("Mis Amigos");
            break;

            case R.id.nav_settings:

            break;

            case R.id.nav_profile:
                Intent intent = new Intent(this,Perfil.class);
                intent.putExtra("uid",Session.getUid());
                startActivity(intent);
            break;

            case R.id.nav_apps:
                fragment = new Fragment_Entretenimiento();
                this.setTitle("Entretenimiento");
            break;
        }

        if(fragment != null){
            change_fragment(fragment);
            item.setChecked(true);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public class UserInfo implements ValueEventListener{

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            String username = dataSnapshot.child("username").getValue().toString();
            tv_username.setText(username);
            tv_email.setText(Session.fb_auth.getCurrentUser().getEmail());
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    private class RequestListener implements ValueEventListener{

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            int count = (int) dataSnapshot.getChildrenCount();
            tv_requests_counter.setText(count + "");
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }


    //Change Conexion Status
    public class Conexion_Status implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                conexion_switch.setText("Conectado");
                online(true);
            }else{
                conexion_switch.setText("Desconectado");
                online(false);
            }
        }
    }

    public void toast(String msj){
        Toast.makeText(this,msj,Toast.LENGTH_LONG).show();
    }

    public void change_fragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.maincontainer,fragment).commit();
    }

    public void online(boolean status){

        DatabaseReference ref = new DBFirebase().get_myconexion();

        if(status){
            ref.child("online").setValue(true);
        }else{
            ref.child("online").setValue(false);
            ref.child("lastime").setValue(Timestamp_utils.current_time());
        }

    }

    public void go_activity_requests(View v){
        //Snackbar.make(v,"GO",Snackbar.LENGTH_LONG).show();
        startActivity(new Intent(this,Requests.class));
    }

    public void download_profileImage(){
        StorageReference storage = new DBStorage().get_profile_picture(Session.getUid());
        storage.getBytes(Constants.MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length,null);
                imgprofile.setImageBitmap(bitmap);
            }
        });
    }

}
