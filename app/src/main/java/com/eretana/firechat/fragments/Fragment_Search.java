package com.eretana.firechat.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.eretana.firechat.R;
import com.eretana.firechat.adapters.Adapter_Search;
import com.eretana.firechat.database.DBFirebase;
import com.eretana.firechat.models.Friend;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Fragment_Search extends Fragment {

    private Bundle bundle;
    private ListView listview;
    private List<Friend> users;
    private Adapter_Search adapter;
    private DatabaseReference ref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_search,container,false);
        init_controls(fragment);
        return fragment;
    }

    public void init_controls(View fragment){
        ref = new DBFirebase().get_searchby_username();
        listview = (ListView) fragment.findViewById(R.id.search_listview);
        users = new ArrayList<>();
        bundle = getArguments();
        adapter = new Adapter_Search(getContext(),users);

        String username = bundle.getString("username");
        int sex = bundle.getInt("sex");
        ref.addListenerForSingleValueEvent(new Search_listener(sex,username));
        listview.setAdapter(adapter);
    }


    private class Search_listener implements ValueEventListener{

        private int search_sex;
        private String search_username;

        public Search_listener(int s, String u){
            this.search_sex = s;
            this.search_username = u;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            users.clear();

            for (DataSnapshot data : dataSnapshot.getChildren()){
                int sexo = Integer.parseInt(data.child("sex").getValue().toString());
                String name = data.child("username").getValue().toString();
                boolean match = name.matches("(.*)" + search_username + "(.*)");

                if(match){

                    Friend friend = new Friend();
                    friend.setUid(data.getKey());
                    friend.setUsername(name);

                    if(sexo == search_sex){
                        users.add(friend);
                    }
                }
            }

            adapter.notifyDataSetChanged();

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

}
