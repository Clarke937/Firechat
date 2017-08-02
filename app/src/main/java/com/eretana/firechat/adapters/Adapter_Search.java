package com.eretana.firechat.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eretana.firechat.Profile;
import com.eretana.firechat.R;
import com.eretana.firechat.models.Friend;

import java.util.List;

/**
 * Created by Edgar on 28/6/2017.
 */

public class Adapter_Search extends BaseAdapter {

    private Context context;
    private List<Friend> users;

    public Adapter_Search(Context context, List<Friend> users) {
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

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_search,parent,false);
        }

        final Friend friend = users.get(position);

        TextView tvusername = (TextView) view.findViewById(R.id.search_username);
        TextView tvcountry = (TextView) view.findViewById(R.id.search_country);

        tvusername.setText(friend.getUsername());
        tvcountry.setText("El Salvador");

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Profile.class);
                intent.putExtra("uid",friend.getUid());
                context.startActivity(intent);
            }
        });

        return view;
    }
}
