package com.eretana.firechat.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

public class Adapter_Post extends BaseAdapter {

    private List<Post> publicaciones;
    private Context context;
    private Bitmap bitmap;

    public Adapter_Post(List<Post> publicaciones, Context c, Bitmap b) {
        this.publicaciones = publicaciones;
        this.context = c;
        this.bitmap = b;
    }

    @Override
    public int getCount() {
        return publicaciones.size();
    }

    @Override
    public Object getItem(int position) {
        return publicaciones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        Post post = publicaciones.get(position);

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_post,parent,false);
        }

        TextView title = (TextView) view.findViewById(R.id.postitem_title);
        CircularImageView civ = (CircularImageView) view.findViewById(R.id.postitem_civ);
        TextView text = (TextView) view.findViewById(R.id.postitem_text);
        ImageView imgv = (ImageView) view.findViewById(R.id.postitem_image);

        if(post.getType() == Post.TYPES.TEXT){
            text.setVisibility(View.VISIBLE);
            text.setText(post.getText());
        }else{
            imgv.setVisibility(View.VISIBLE);
            imgv.setImageBitmap(post.getImage());
        }

        civ.setImageBitmap(bitmap);
        title.setText(post.getTitle());

        return view;
    }
}
