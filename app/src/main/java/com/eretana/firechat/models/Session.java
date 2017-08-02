package com.eretana.firechat.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Edgar on 23/6/2017.
 */

public final class Session {

    public static FirebaseAuth fb_auth;

    public static String getUid(){
        return fb_auth.getCurrentUser().getUid();
    }

    public static String getEmail(){
        return fb_auth.getCurrentUser().getEmail();
    }


}
