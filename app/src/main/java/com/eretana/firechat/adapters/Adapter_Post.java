package com.eretana.firechat.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.eretana.firechat.Post_viewer;
import com.eretana.firechat.R;
import com.eretana.firechat.models.Post;
import com.eretana.firechat.utils.Timestamp_utils;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edgar on 3/7/2017.
 */

public class Adapter_Post extends RecyclerView.Adapter<Adapter_Post.VHolder>{

    private List<Post> publicaciones;
    private Context context;

    public Adapter_Post(List<Post> publicaciones, Context context) {
        this.publicaciones = publicaciones;
        this.context = context;
    }

    @Override
    public VHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_post,parent,false);
        return new VHolder(v);
    }

    @Override
    public void onBindViewHolder(VHolder holder, int position) {
        Post post = publicaciones.get(position);
        holder.civ.setImageBitmap(post.getImage());
        holder.tv_title.setText(post.getTitle());

        if(post.getType() == Post.TYPES.TEXT){
            holder.tv_text.setVisibility(View.VISIBLE);
            holder.tv_text.setText(post.getText());
        }else{

        }
    }

    @Override
    public int getItemCount() {
        return publicaciones.size();
    }

    public class VHolder extends RecyclerView.ViewHolder{

        private CircularImageView civ;
        private TextView tv_title;
        private ImageView imgview;
        private TextView tv_text;
        private FloatingActionButton fab1,fab2;

        public VHolder(View view) {
            super(view);
            this.civ = (CircularImageView) view.findViewById(R.id.postitem_civ);
            this.tv_title = (TextView) view.findViewById(R.id.postitem_title);
            this.imgview = (ImageView) view.findViewById(R.id.postitem_image);
            this.tv_text = (TextView) view.findViewById(R.id.postitem_text);
        }



    }

}
