package com.eretana.firechat.database;

import com.eretana.firechat.models.Session;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Edgar on 2/7/2017.
 */

public class DBStorage {

    private StorageReference storage;

    public DBStorage(){
        storage = FirebaseStorage.getInstance().getReference();
    }

    public StorageReference get_profile_picture(String uid){
        return storage.child("profile_pictures").child(uid + ".jpg");
    }

    public StorageReference get_post_image(String key){
        return storage.child("post_pictures").child(key + ".jpg");
    }



}
