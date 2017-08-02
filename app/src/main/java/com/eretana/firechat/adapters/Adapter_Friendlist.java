package com.eretana.firechat.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eretana.firechat.R;
import com.eretana.firechat.database.DBStorage;
import com.eretana.firechat.models.Constants;
import com.eretana.firechat.models.Friend;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edgar on 24/6/2017.
 */

public class Adapter_Friendlist extends BaseAdapter {

    private List<Friend> friends = new ArrayList<>();
    private Context context;

    public Adapter_Friendlist(List<Friend> fs, Context cx){
        this.friends = fs;
        this.context = cx;
    }

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Object getItem(int position) {
        return friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_friend,parent,false);
        }

        Friend item = friends.get(position);

        final CircularImageView civ = (CircularImageView) view.findViewById(R.id.frienditem_photo);
        TextView tv_username = (TextView) view.findViewById(R.id.frienditem_username);
        TextView tv_email = (TextView) view.findViewById(R.id.frienditem_email);
        TextView tv_conexion = (TextView) view.findViewById(R.id.frienditem_conexion);

        tv_username.setText(item.getUsername());
        tv_email.setText(item.getEmail());

        if(item.isOnline()){
            tv_conexion.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorGreen));
            tv_conexion.setText("On");
        }else{
            tv_conexion.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorHell));
            tv_conexion.setText(item.getLastime());
        }

        return view;
    }
}
