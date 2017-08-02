package com.eretana.firechat.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eretana.firechat.R;
import com.eretana.firechat.database.DBFirebase;
import com.eretana.firechat.database.DBStorage;
import com.eretana.firechat.models.Constants;
import com.eretana.firechat.models.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

/**
 * Created by Edgar on 19/7/2017.
 */

public class Adapter_Requests extends BaseAdapter {

    private Context context;
    private List<Request> solicitudes;
    private StorageReference storage;

    public Adapter_Requests(Context context, List<Request> solicitudes) {
        this.context = context;
        this.solicitudes = solicitudes;
    }

    @Override
    public int getCount() {
        return solicitudes.size();
    }

    @Override
    public Object getItem(int position) {
        return solicitudes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = null;

        if(convertView == null){
            v = LayoutInflater.from(context).inflate(R.layout.item_request,parent,false);

            final ImageView img = (ImageView) v.findViewById(R.id.request_item_image);
            TextView tvusername = (TextView) v.findViewById(R.id.request_item_username);
            TextView tvdescription = (TextView) v.findViewById(R.id.request_item_description);
            //Button btn_detalles = (Button) v.findViewById(R.id.);
            Button btn_aceptar = (Button) v.findViewById(R.id.request_item_btn_aceptar);
            //Button btn_cancelar = (Button) v.findViewById(R.id.);


            final Request request = solicitudes.get(position);
            tvusername.setText(request.getUsername());
            tvdescription.setText(request.getDescription());

            storage = new DBStorage().get_profile_picture(request.getUid());

            storage.getBytes(Constants.MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    img.setImageBitmap(bitmap);
                }
            });

            btn_aceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v,request.getUsername() + " es tu amigo ahora",Snackbar.LENGTH_LONG).show();
                    new DBFirebase().accept_friend_request(request,context);
                    solicitudes.remove(position);
                    Adapter_Requests.this.notifyDataSetChanged();
                }
            });


        }

        return v;
    }
}
