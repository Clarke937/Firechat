package com.eretana.firechat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.eretana.firechat.database.DBFirebase;
import com.eretana.firechat.database.SQLApp;
import com.eretana.firechat.models.User;
import com.eretana.firechat.utils.LayoutParams_utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Miniapp_lovefinder extends AppCompatActivity implements OnRangeSeekbarChangeListener{

    CrystalRangeSeekbar range;
    TextView tvmin,tvmax;
    RadioGroup sexgroup;
    Spinner spinCountry;
    LinearLayout layoutintereses;
    private DatabaseReference ref;
    private ValueEventListener listener;
    private ProgressDialog pgdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miniapp_lovefinder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Love Finder");
        init_controls();
    }

    public void init_controls(){
        range = (CrystalRangeSeekbar) findViewById(R.id.app_lf_edadseekbar);
        tvmin = (TextView) findViewById(R.id.app_lf_edadmin);
        tvmax = (TextView) findViewById(R.id.app_lf_edadmax);
        sexgroup = (RadioGroup) findViewById(R.id.app_lf_sexgroup);
        spinCountry = (Spinner) findViewById(R.id.app_lf_paises);
        layoutintereses = (LinearLayout) findViewById(R.id.app_lf_intereses);
        pgdialog = new ProgressDialog(this);
        ref = new DBFirebase().get_users();

        range.setOnRangeSeekbarChangeListener(this);
        populate_intereses();
    }


    @Override
    public void valueChanged(Number minValue, Number maxValue) {
        int min = minValue.intValue();
        int max = maxValue.intValue();
        tvmin.setText("" + min);
        tvmax.setText("" + max);
    }

    public void populate_intereses(){
        layoutintereses.removeAllViews();
        for(String item : new SQLApp(this).get_intereses()){
            CheckBox check = new CheckBox(this);
            check.setText(item);
            check.setChecked(false);
            layoutintereses.addView(check);
        }
    }

    public void search_love(View v){

        int edadmin = range.getSelectedMinValue().intValue();
        int edadmax = range.getSelectedMaxValue().intValue();

        int sexo = 0;

        switch (sexgroup.getCheckedRadioButtonId()){
            case R.id.rb_men:
                sexo = 1;
            break;
            case R.id.rb_other:
                sexo = 2;
            break;
        }

        String pais = spinCountry.getSelectedItem().toString();
        List<String> intereses = new ArrayList<>();

        int childs = layoutintereses.getChildCount();

        for(int i = 0; i < childs; i++){
            CheckBox check = (CheckBox) layoutintereses.getChildAt(i);
            if(check.isChecked()){
                intereses.add(check.getText().toString());
            }
        }

        listener = new Busqueda(sexo,pais,edadmin,edadmax,intereses);
        ref.addListenerForSingleValueEvent(listener);
    }



    private class Busqueda implements ValueEventListener {

        private int sexo;
        private String pais;
        private int edadmin;
        private int edadmax;
        private List<String> intereses;
        private ArrayList<User> users;

        public Busqueda(int sexo, String pais, int edadmin, int edadmax, List<String> intereses) {
            this.sexo = sexo;
            this.pais = pais;
            this.edadmin = edadmin;
            this.edadmax = edadmax;
            this.intereses = intereses;
            this.users = new ArrayList<>();
        }

        @Override
        public void onDataChange(DataSnapshot ds) {

            int userscount = (int) ds.getChildrenCount();
            int iterador = 0;
            showProgress();

            for (DataSnapshot d: ds.getChildren()) {

                String sex = d.child("sex").getValue().toString();
                String country = d.child("country").getValue().toString();
                int age = Integer.parseInt(d.child("age").getValue().toString());
                boolean inrange = age >= edadmin && age <= edadmax;

                List<String> interests = new ArrayList<>();

                for (DataSnapshot in : d.child("interest").getChildren()){
                    interests.add(in.getKey());
                }

                if(sex.equals("" + sexo) && country.equals(pais) && inrange){
                    if(interests.containsAll(intereses)){

                        Log.e("APPLF_USERS",d.getKey());

                        User u = new User();
                        u.setUid(d.getKey());
                        u.setUsername(d.child("username").getValue().toString());
                        u.setCountry(d.child("country").getValue().toString());
                        u.setAge(Integer.parseInt(d.child("age").getValue().toString()));

                        users.add(u);
                    }
                }

                iterador++;
                int progress = iterador * 100 / userscount;
                setProgreso(progress);
            }

            closeProgress();
            show_results(users);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    public void showProgress(){
        pgdialog.setTitle("Busqueda en Progreso...");
        pgdialog.setIndeterminate(false);
        pgdialog.setMax(100);
        //pgdialog.setMessage("Espere unos momentos");
        pgdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pgdialog.setCanceledOnTouchOutside(false);

        pgdialog.show();
    }

    public void setProgreso(int pg){
        pgdialog.setProgress(pg);
    }


    public void closeProgress(){
        pgdialog.dismiss();
    }

    public void show_results(ArrayList<User> users){

        if(users.size() > 0){
            Intent intent = new Intent(this,Lovefinder_result.class);
            intent.putExtra("users",users);
            startActivity(intent);
        }else{
            Toast.makeText(this,"Ningun Resultado Encontrado",Toast.LENGTH_LONG).show();
        }

    }




}
