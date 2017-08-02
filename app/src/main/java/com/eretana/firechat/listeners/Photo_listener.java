package com.eretana.firechat.listeners;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.eretana.firechat.models.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Edgar on 9/7/2017.
 */

public class Photo_listener {

    private StorageReference storage;
    private ImageView imageview;

    public Photo_listener(ImageView imageview) {
        this.imageview = imageview;
        storage = FirebaseStorage.getInstance().getReference().child("profile_pictures");
    }

    public void setPhoto(String uid){

        storage.child(uid + ".jpg").getBytes(Constants.MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                imageview.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    };





}
