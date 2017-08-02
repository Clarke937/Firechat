package com.eretana.firechat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eretana.firechat.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import com.eretana.firechat.R;

/**
 * Created by Edgar on 31/7/2017.
 */

public class Adapter_Lovefinder extends BaseAdapter{


    private Context context;
    private ArrayList<User> users;

    public Adapter_Lovefinder(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        User user = users.get(position);

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_loveuser,parent,false);
        }

        TextView tvusername = (TextView) view.findViewById(R.id.lovefinder_results_username);
        ImageView imgv = (ImageView) view.findViewById(R.id.lovefinder_results_image);
        TextView tvdata = (TextView) view.findViewById(R.id.lovefinder_results_data);

        String data = user.getAge() + " Años | " + user.getCountry();

        if(user.getImage() != null){
            imgv.setImageBitmap(user.getImage());
        }

        tvusername.setText(user.getUsername());
        tvdata.setText(data);

        return view;
    }

}
