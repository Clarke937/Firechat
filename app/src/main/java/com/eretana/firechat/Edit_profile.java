package com.eretana.firechat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.eretana.firechat.adapters.Adapter_Photomenu;
import com.eretana.firechat.database.DBFirebase;
import com.eretana.firechat.database.DBStorage;
import com.eretana.firechat.database.SQLUser;
import com.eretana.firechat.models.Constants;
import com.eretana.firechat.models.Session;
import com.eretana.firechat.utils.LayoutParams_utils;
import com.eretana.firechat.utils.MyDialogs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Edit_profile extends AppCompatActivity implements ValueEventListener{

    private FloatingActionButton fab;
    private Toolbar toolbar;
    private Spinner spin_edad,spin_pais;
    private RadioGroup group_sexo;
    private EditText edit_description;
    private LinearLayout layout_intereses;
    private CircularImageView civ;

    //DATA
    private String uid;
    private String mode;
    private List<String> paises;
    private List<String> intereses;
    private List<Integer> edades;

    //Objects
    private DatabaseReference appref;
    private ArrayAdapter<String> paisadapter;
    private ArrayAdapter<Integer> edadesadapter;
    private SQLUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mode = getIntent().getStringExtra("mode");

        if(mode.equals("edit")){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            this.setTitle("Edicion de Perfil");
        }else{
            this.setTitle("Completa tu Perfil");
        }

        init_controls();
    }


    public void init_controls(){

        //Controls
        fab = (FloatingActionButton) findViewById(R.id.fab);
        spin_edad = (Spinner) findViewById(R.id.edituser_spin_edad);
        group_sexo = (RadioGroup) findViewById(R.id.radiogroup_sex);
        spin_pais = (Spinner) findViewById(R.id.edituser_spin_country);
        edit_description = (EditText) findViewById(R.id.edituser_description);
        layout_intereses = (LinearLayout) findViewById(R.id.edituser_intereses_content);
        civ = (CircularImageView) findViewById(R.id.edituser_image);

        //Data
        uid = getIntent().getStringExtra("uid");
        paises = new ArrayList<>();
        intereses = new ArrayList<>();
        edades = new ArrayList<>();

        for(int i = 18; i <= 99; i++){
            edades.add(i);
        }

        //Objects
        paisadapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,paises);
        edadesadapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_list_item_1,edades);
        appref = new DBFirebase().get_app();
        appref.addListenerForSingleValueEvent(this);
        user = new SQLUser(this);

        //Setters
        spin_pais.setAdapter(paisadapter);
        spin_edad.setAdapter(edadesadapter);

        if(mode.equals("edit")){
            fill_user_data();
            download_image();
        }else if(mode.equals("new")){

        }
    }

    public void fill_user_data(){

        spin_edad.setSelection(user.get_age() - 18);
        int sexo = user.get_sex();

        switch (sexo){
            case 0:
                group_sexo.check(R.id.sexrb_mujer);
            break;
            case 1:
                group_sexo.check(R.id.sexrb_hombre);
            break;
            case 2:
                group_sexo.check(R.id.sexrb_otro);
            break;
        }

        edit_description.setText(user.get_description());
    }


    public void save_data(View v){

        //GET DATA
        int edad = Integer.parseInt(spin_edad.getSelectedItem().toString());
        int sexo = 0;

        switch (group_sexo.getCheckedRadioButtonId()){
            case R.id.sexrb_hombre:
                sexo = 1;
            break;
            case R.id.sexrb_otro:
                sexo = 2;
                break;
        }

        String pais = spin_pais.getSelectedItem().toString();
        String description = edit_description.getText().toString();

        user.update(sexo,edad,pais,description);

        if(mode.equals("new")){
            Intent intent = new Intent(this,Application.class);
            startActivity(intent);
        }else if(mode.equals("edit")){
            Toast.makeText(this,"Informacion Actualizada",Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                Intent intent = new Intent(this,Perfil.class);
                intent.putExtra("uid", Session.getUid());
                startActivity(intent);
            break;
        }

        return true;
    }

    @Override
    public void onDataChange(DataSnapshot data) {

        for(DataSnapshot d : data.child("countries").getChildren()){
            paises.add(d.getKey());
        }
        paisadapter.notifyDataSetChanged();

        if(mode.equals("edit")){
            String pais = user.get_country();
            spin_pais.setSelection(paises.indexOf(pais));
        }


        layout_intereses.removeAllViews();
        for(DataSnapshot d: data.child("interest").getChildren()){
            CheckBox check = new CheckBox(this);
            check.setText(d.getKey());
            check.setLayoutParams(LayoutParams_utils.setMargins(0,5,0,0));
            layout_intereses.addView(check);
        }


    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }


    public void download_image(){

        StorageReference storage = new DBStorage().get_profile_picture(Session.getUid());

        storage.getBytes(Constants.MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                civ.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }


    public void change_photo(final View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nueva Imagen");

        String[] opciones = new String[]{"Galeria","Fotografia"};
        Integer[] iconos = new Integer[]{
                R.drawable.ic_menu_camera,
                R.drawable.ic_menu_gallery};

        ListAdapter adapter = new Adapter_Photomenu(this,opciones,iconos);

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(which == 1){
                    choise_single_image();
                }else{
                    Snackbar.make(v,"Opcion aun no valida" + which,Snackbar.LENGTH_LONG).show();
                }
            }
        });

        builder.create().show();
    }


    public void choise_single_image() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccionar Imagen"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),data.getData());
                preview_image(bitmap);
            }catch (IOException e){

            }
        }
    }


    private void preview_image(final Bitmap bitmap){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nueva Imagen");
        View view = LayoutInflater.from(this).inflate(R.layout.vista_preview_image_dialog,null,false);
        ImageView image = (ImageView) view.findViewById(R.id.image_preview);
        image.setImageBitmap(bitmap);
        builder.setView(view);
        builder.setNegativeButton("Cancelar",null);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                upload_image(bitmap);
            }
        });

        builder.create().show();

    }


    public void upload_image(final Bitmap bitmap){

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setTitle("Actualizando Imagen");
        dialog.show();

        StorageReference storage = new DBStorage().get_profile_picture(uid);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] databytes = stream.toByteArray();

        storage.putBytes(databytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                dialog.dismiss();
                civ.setImageBitmap(bitmap);
            }
        });
    }
}
