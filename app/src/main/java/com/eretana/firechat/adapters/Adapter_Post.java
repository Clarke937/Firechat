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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.eretana.firechat.Post_viewer;
import com.eretana.firechat.R;
import com.eretana.firechat.database.DBStorage;
import com.eretana.firechat.models.Constants;
import com.eretana.firechat.models.Emojis;
import com.eretana.firechat.models.Post;
import com.eretana.firechat.utils.Launcher_Activity;
import com.eretana.firechat.utils.MyBitmap;
import com.eretana.firechat.utils.MyNums;
import com.eretana.firechat.utils.Timestamp_utils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
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
        View v = LayoutInflater.from(context).inflate(R.layout.item_post2,parent,false);
        return new VHolder(v);
    }

    @Override
    public void onBindViewHolder(final VHolder holder, final int position) {
        final Post post = publicaciones.get(position);
        holder.title.setText(post.getTitle());

        long miliseconds = Timestamp_utils.current_time() - post.getTimestamp();
        holder.timer.setText("Publicado hace: " + Timestamp_utils.elapsed_time(miliseconds));
        holder.emojiview.setImageDrawable(new Emojis(context).getEmoji(post.getEmoji()));

        if(post.getType() == Post.TYPES.TEXT){
            holder.text.setVisibility(View.VISIBLE);
            holder.text.setText(post.getText());
        }else{
            holder.imageview.setVisibility(View.VISIBLE);
            if(post.getText().length() > 0){
                holder.text.setVisibility(View.VISIBLE);
                holder.text.setText(post.getText());
            }

            StorageReference storage = new DBStorage().get_post_image(post.getId());
            storage.getBytes(Constants.MEGABYTE * 2).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    holder.imageview.setImageBitmap(MyBitmap.getBitmap(bytes));
                    notifyDataSetChanged();
                }
            });
        }

        holder.btncomments.setText(MyNums.Resume(post.getComments()));
        holder.btncomments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Launcher_Activity(context).goViewPost(post.getUid(),post.getId());
            }
        });

        holder.btnlikes.setText(MyNums.Resume(post.getLikes()));
    }

    @Override
    public int getItemCount() {
        return publicaciones.size();
    }

    public class VHolder extends RecyclerView.ViewHolder{

        ImageView emojiview,imageview;
        TextView title,timer,text;
        Button btnlikes,btncomments;


        public VHolder(View view) {
            super(view);
            emojiview = (ImageView) view.findViewById(R.id.post2_emoji);
            imageview = (ImageView) view.findViewById(R.id.post2_image);
            title = (TextView) view.findViewById(R.id.post2_title);
            timer = (TextView) view.findViewById(R.id.post2_timer);
            text = (TextView) view.findViewById(R.id.post2_text);

            btnlikes = (Button) view.findViewById(R.id.post2_likes);
            btncomments = (Button) view.findViewById(R.id.post2_comments);

        }
    }

}
