package com.eretana.firechat.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.eretana.firechat.Edit_profile;
import com.eretana.firechat.R;
import com.eretana.firechat.database.DBFirebase;
import com.eretana.firechat.database.SQLUser;
import com.eretana.firechat.models.Session;
import com.google.firebase.database.DatabaseReference;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Edgar on 9/7/2017.
 */

public class MyDialogs {



    public BottomSheetDialog CommentDialog(final Context c,final String uid, final String postid){

        final BottomSheetDialog dialog = new BottomSheetDialog(c);
        View v = LayoutInflater.from(c).inflate(R.layout.vista_comment,null);
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.comment_fab);
        final EditText editText = (EditText) v.findViewById(R.id.comment_edittext);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = editText.getText().toString();
                DatabaseReference ref = new DBFirebase().get_comment(uid,postid);

                Map<String,Object> data = new TreeMap<>();
                data.put("by", Session.getUid());
                data.put("timestamp",Timestamp_utils.current_time());
                data.put("text",comment);
                data.put("date",Timestamp_utils.current_date());
                data.put("username", new SQLUser(c).get_username());

                ref.setValue(data);
                editText.setText("");
                dialog.dismiss();
            }
        });


        dialog.setContentView(v);
        return dialog;
    }


    public void Preview_ImageProfile_Dialog(Context c, Bitmap bitmap){

    }

}
