package com.eretana.firechat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.eretana.firechat.database.SQLApp;
import com.eretana.firechat.database.SQLUser;
import com.eretana.firechat.models.Session;
import com.eretana.firechat.utils.Conexion_utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splash();
    }

    public void splash(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isConected()){
                    entry();
                }else{
                    ConexionAlert();
                }
            }
        },3000);

    }

    public boolean isConected(){
        return new Conexion_utils(this).isConnected();
    }

    public void entry() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            startActivity(new Intent(this, Authentication.class));
        } else {
            Session.fb_auth = FirebaseAuth.getInstance();
            new SQLUser(this).sync();
            new SQLApp(this).sync();

            startActivity(new Intent(this, Application.class));
        }

        this.finish();
    }

    public void ConexionAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.vista_warning_alert, null);
        TextView text = (TextView) view.findViewById(R.id.warning_text);
        text.setText("Verifica tu conexion a internet");
        builder.setView(view);

        builder.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Splash.this.finish();
                System.exit(0);
            }
        });

        builder.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isConected()) {
                    entry();
                } else {
                    Toast.makeText(Splash.this, "Intentalo mas tarde", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.create().show();
    }


}
