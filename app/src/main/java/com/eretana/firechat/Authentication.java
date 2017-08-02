package com.eretana.firechat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import android.widget.EditText;
import android.widget.Toast;

import com.eretana.firechat.adapters.Adapter_Auth;
import com.eretana.firechat.database.DBFirebase;
import com.eretana.firechat.database.SQLUser;
import com.eretana.firechat.models.Session;
import com.eretana.firechat.utils.Form_utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Authentication extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth auth;

    private Adapter_Auth mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        this.setTitle("Firechat Login");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new Adapter_Auth(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        auth = FirebaseAuth.getInstance();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(auth.getCurrentUser() != null){
            welcome();
        }
    }

    @Override
    public void onClick(View v) {
        switch (mViewPager.getCurrentItem()){
            case 0:
                this.login();
            break;

            case 1:
                this.create();
            break;
        }
    }

    public void login() {

        final EditText et_email = (EditText) findViewById(R.id.auth_email);
        final EditText et_passw = (EditText) findViewById(R.id.auth_password);

        if(Form_utils.are_fill(et_email,et_passw)){

            String email = et_email.getText().toString();
            String passw = et_passw.getText().toString();

            auth.signInWithEmailAndPassword(email,passw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        welcome();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    if(e instanceof FirebaseAuthInvalidCredentialsException){
                        toast("CONTRASEÑA INCONRRECTA");
                    }else if(e instanceof FirebaseAuthInvalidUserException){
                        toast("NO EXISTE ESTE USUARIO");
                    }

                }
            });

        }else{
            toast("Complete el formulario");
        }

    }


    public void create(){

        EditText et_username = (EditText) findViewById(R.id.create_username);
        EditText et_email = (EditText) findViewById(R.id.create_email);
        EditText et_password = (EditText) findViewById(R.id.create_password);

        if(Form_utils.are_fill(et_username,et_email,et_password)){

            final String username = et_username.getText().toString();
            final String email = et_email.getText().toString();
            String password = et_password.getText().toString();

            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        new DBFirebase().create_user(username,auth.getCurrentUser());
                        continue_register(auth.getCurrentUser().getUid(),username,email);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    if(e instanceof FirebaseAuthUserCollisionException){
                        toast(getString(R.string.err_email_exist));
                    }else if( e instanceof FirebaseAuthWeakPasswordException){
                        toast(((FirebaseAuthWeakPasswordException) e).getReason());
                    }

                }
            });


        }


    }

    public void continue_register(String uid, String username,String email) {
        Session.fb_auth = auth;
        SQLUser sql = new SQLUser(this);

        sql.new_user(uid,username,email);
        //sql.show();

        Intent intent = new Intent(this,Edit_profile.class);
        intent.putExtra("uid",uid);
        intent.putExtra("mode","new");
        startActivity(intent);
    }


    public void welcome(){
        Session.fb_auth = auth;
        SQLUser sql = new SQLUser(this);
        sql.sync();
        startActivity(new Intent(this,Application.class));
        this.finish();
    }

    public void toast(String msj){
        Toast.makeText(this,msj,Toast.LENGTH_SHORT).show();
    }

}
