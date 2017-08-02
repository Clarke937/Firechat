package com.eretana.firechat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.eretana.firechat.R;

import com.eretana.firechat.models.Comment;

import java.util.List;

/**
 * Created by Edgar on 9/7/2017.
 */

public class Adapter_Comments extends BaseAdapter{

    private Context context;
    private List<Comment> comments;

    public Adapter_Comments(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_comment,parent,false);
        }

        Comment c = comments.get(position);

        ImageView image = (ImageView) view.findViewById(R.id.comment_photo);
        TextView tvusername = (TextView) view.findViewById(R.id.comment_username);
        TextView tvtext = (TextView) view.findViewById(R.id.comment_text);

        tvusername.setText(c.getUsername());
        tvtext.setText(c.getText());

        return view;
    }
}
