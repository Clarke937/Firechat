package com.eretana.firechat.database;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.eretana.firechat.models.Post;
import com.eretana.firechat.models.Request;
import com.eretana.firechat.models.Session;
import com.eretana.firechat.utils.MyBitmap;
import com.eretana.firechat.utils.Timestamp_utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Created by Edgar on 24/6/2017.
 */

public class DBFirebase {

    private DatabaseReference reference;

    public DBFirebase(){
        reference = FirebaseDatabase.getInstance().getReference();
    }

    public DatabaseReference get_friendlist(String uid){
        return reference.child("friends").child(uid);
    }

    public DatabaseReference get_online() {
        return reference.child("online");
    }

    public DatabaseReference get_myuser(){
        return reference.child("users").child(Session.fb_auth.getCurrentUser().getUid());
    }

    public DatabaseReference get_conexion(){
        return reference.child("conexion");
    }

    public DatabaseReference get_myconexion(){
        return reference.child("conexion").child(Session.fb_auth.getCurrentUser().getUid());
    }

    public DatabaseReference get_userConexion(String uid){
        return reference.child("conexion").child(uid);
    }

    public DatabaseReference get_hotlikes(){
        return reference.child("hotlikes").child(Session.fb_auth.getCurrentUser().getUid());
    }

    public DatabaseReference get_searchby_username(){
        return reference.child("searchs").child("by_username");
    }

    public DatabaseReference get_user(String uid){
        return reference.child("users").child(uid);
    }

    public DatabaseReference get_requests(){
        return reference.child("requests");
    }

    public Query get_my_requests(){
        return reference.child("requests").child(Session.getUid()).orderByKey();
    }

    public Query get_my_posts(){
        return  reference.child("posts").child(Session.getUid()).orderByKey();
    }

    public Query get_user_posts(String uid){
        return reference.child("posts").child(uid).orderByKey();
    }

    public DatabaseReference get_post(String uid,String id){
        return reference.child("posts").child(uid).child(id);
    }

    public DatabaseReference get_comment(String uid,String postid){
        return reference.child("posts").child(uid).child(postid).child("comments").push();
    }

    public DatabaseReference get_app(){
        return reference.child("app");
    }

    public void create_user(String username, FirebaseUser user){

        DatabaseReference users = reference.child("users").child(user.getUid());
        users.child("username").setValue(username);
        users.child("email").setValue(user.getEmail());
        users.child("start_date").setValue(Timestamp_utils.current_date());

        DatabaseReference conexion = reference.child("conexion").child(user.getUid());
        conexion.child("online").setValue(true);
        conexion.child("lastime").setValue(0);

        DatabaseReference searchs = reference.child("searchs").child("by_username").child(user.getUid());
        searchs.child("sex").setValue(2);
        searchs.child("username").setValue(username);

        reference.child("coins").child(user.getUid()).setValue(1000);
    }


    public void accept_friend_request(Request r, Context c){
        reference.child("requests").child(Session.getUid()).child(r.getUid()).setValue(null);

        DatabaseReference ref = get_friendlist(Session.getUid()).child(r.getUid());

        String date = Timestamp_utils.current_date();

        Map<String,String> data = new TreeMap<>();
        data.put("start_date",date);
        data.put("username",r.getUsername());
        data.put("email",r.getEmail());
        ref.setValue(data);

        DatabaseReference friendref = get_friendlist(r.getUid()).child(Session.getUid());
        Map<String,Object> mydata = new TreeMap<>();
        SQLUser myuser = new SQLUser(c);

        mydata.put("start_date",date);
        mydata.put("username",myuser.get_username());
        mydata.put("email",myuser.get_email());

        friendref.setValue(mydata);
    }

    public void set_online(boolean status){
        Map<String,Object> data = new TreeMap<>();
        data.put("online",status);

        if(!status) {
            data.put("lastime",Timestamp_utils.current_time());
        }

        reference.child("conexion").child(Session.getUid()).setValue(data);
    }


    public DatabaseReference get_chats(){
        return reference.child("chats");
    }


    public Query get_chat(String key){
        return reference.child("chats").child(key).child("messages").orderByKey().limitToLast(100);
    }

    public void send_message(String key, Map<String,Object> data){
        reference.child("chats").child(key).child("messages").push().setValue(data);
        reference.child("chats").child(key).child("last_message").setValue(data);
    }

    public DatabaseReference get_intereses(){
        return reference.child("app").child("interest");
    }

    public DatabaseReference get_users(){
        return reference.child("users");
    }


    public void new_post(Post p){
        reference = reference.child("posts").child(Session.getUid()).push();
        Map<String,Object> data = new TreeMap<>();

        data.put("title",p.getTitle());
        data.put("emoji",p.getEmoji());
        data.put("text",p.getText());
        data.put("timestamp",Timestamp_utils.current_time());
        data.put("date",Timestamp_utils.current_date());
        data.put("likes",0);

        if(p.getType() == Post.TYPES.TEXT){
            data.put("type","text");
        }else{
            data.put("type","image");
            data.put("imageurl",reference.getKey() + ".jpg");
            StorageReference storage = new DBStorage().get_post_image(reference.getKey());
            storage.putBytes(MyBitmap.getBytes(p.getImage()));
        }

        reference.setValue(data);
    }

}
