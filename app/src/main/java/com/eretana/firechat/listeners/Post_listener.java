package com.eretana.firechat.listeners;

import android.util.Log;

import com.eretana.firechat.adapters.Adapter_Post;
import com.eretana.firechat.models.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by Edgar on 4/7/2017.
 */

public class Post_listener implements ValueEventListener {

    private Adapter_Post adapter;
    private List<Post> posts;

    public Post_listener(Adapter_Post a, List<Post> p) {
        this.adapter = a;
        this.posts = p;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        posts.clear();
        final String uid = dataSnapshot.getKey();

        for(DataSnapshot data : dataSnapshot.getChildren()){

            String key = data.getKey();
            String title = data.child("title").getValue().toString();
            long timestamp = Long.parseLong(data.child("timestamp").getValue().toString());
            String text = data.child("text").getValue().toString();

            Post post = new Post();
            post.setUid(uid);
            post.setId(key);
            post.setTitle(title);
            post.setTimestamp(timestamp);
            post.setText(text);
            post.setType(Post.TYPES.TEXT);

            posts.add(post);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
