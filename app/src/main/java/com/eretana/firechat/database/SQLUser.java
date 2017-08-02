package com.eretana.firechat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.eretana.firechat.models.Session;
import com.eretana.firechat.utils.Timestamp_utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Edgar on 14/7/2017.
 */

public class SQLUser extends SQLiteOpenHelper implements ValueEventListener{

    private static int VERSION = 2;
    private SQLiteDatabase sql;
    private DatabaseReference useref;

    public SQLUser(Context context){
        super(context,"DBUser",null,VERSION);
        sql = this.getWritableDatabase();
        useref = new DBFirebase().get_myuser();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String user_table = "CREATE TABLE User(" +
                "uid INTEGER, " +
                "username TEXT, " +
                "email TEXT, " +
                "sex INTEGER, " +
                "age INTEGER, " +
                "country TEXT, " +
                "start_date TEXT, " +
                "description TEXT, " +
                "photo_url TEXT" +

                ")";

        db.execSQL(user_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS User");
        onCreate(db);
        sync();
    }

    public void sync(){
        if(useref == null){
            useref = new DBFirebase().get_myuser();
        }
        useref.addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(DataSnapshot data) {

        ContentValues cv = new ContentValues();
        int count = sql.rawQuery("Select * from User",null).getCount();
        cv.put("uid",data.getKey());
        cv.put("username",data.child("username").getValue().toString());
        cv.put("email",data.child("email").getValue().toString());
        cv.put("sex",Integer.parseInt(data.child("sex").getValue().toString()));
        cv.put("age",Integer.parseInt(data.child("age").getValue().toString()));
        cv.put("country",data.child("country").getValue().toString());
        cv.put("start_date",data.child("start_date").getValue().toString());
        cv.put("description",data.child("description").getValue().toString());
        //cv.put("photo_url",data.child("photo_url").getValue().toString());

        if(count == 0){
            sql.insert("User",null,cv);
        }else{
            sql.update("User",cv,"uid = ?",new String[]{data.getKey()});
        }

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public void new_user(String uid, String username, String email){
        ContentValues cv = new ContentValues();
        cv.put("uid",uid);
        cv.put("username",username);
        cv.put("email",email);
        cv.put("start_date", Timestamp_utils.current_date());

        sql.insert("User",null,cv);
    }

    public void show(){
        Cursor c = sql.rawQuery("Select * from User",null);

        try{

            Log.e("SHOWUSER: ","*******************");

            while (c.moveToNext()){
                Log.e("UID: ",c.getString(0));
                Log.e("USERNAME :",c.getString(1));
                Log.e("EMAIL:",c.getString(2));
                Log.e("SEX:",c.getInt(3) + "");
                Log.e("AGE:",c.getInt(4) + "");
                Log.e("COUNTRY:",c.getString(5));
                Log.e("START_DATE:",c.getString(6));
                Log.e("DESCRIPTION:",c.getString(7));
                Log.e("PHOTO_URL:",c.getString(8));
                Log.e("-----------","-----------");
            }

        }catch (NullPointerException e){

        }

    }

    public void update(int sex, int age, String country, String desc){
        ContentValues cv = new ContentValues();
        cv.put("sex",sex);
        cv.put("age",age);
        cv.put("country",country);
        cv.put("description",desc);
        sql.update("User",cv,"uid = ?",new String[]{Session.getUid()});

        useref.child("sex").setValue(sex);
        useref.child("age").setValue(age);
        useref.child("country").setValue(country);
        useref.child("description").setValue(desc);

        DatabaseReference searchref = new DBFirebase().get_searchby_username();
        searchref.child(Session.getUid()).child("sex").setValue(sex);
    }

    //GETTERS

    public String get_username(){
        String query = "Select username from User where uid = ?";
        Cursor c = sql.rawQuery(query,new String[]{Session.getUid()});

        if(c.moveToNext()){
            return c.getString(0);
        }

        return "";
    }

    public String get_email(){
        String query = "Select email from User where uid = ?";
        Cursor c = sql.rawQuery(query,new String[]{Session.getUid()});

        if(c.moveToNext()){
            return c.getString(0);
        }

        return "";
    }

    public int get_sex(){
        String query = "Select sex from User where uid = ?";
        Cursor c = sql.rawQuery(query,new String[]{Session.getUid()});

        if(c.moveToNext()){
            return c.getInt(0);
        }

        return -1;
    }

    public int get_age(){
        String query = "Select age from User where uid = ?";
        Cursor c = sql.rawQuery(query,new String[]{Session.getUid()});

        if(c.moveToNext()){
            return c.getInt(0);
        }

        return -1;
    }

    public String get_country(){
        String query = "Select country from User where uid = ?";
        Cursor c = sql.rawQuery(query,new String[]{Session.getUid()});

        if(c.moveToNext()){
            return c.getString(0);
        }

        return "";
    }

    public String get_start_date(){
        String query = "Select start_date from User where uid = ?";
        Cursor c = sql.rawQuery(query,new String[]{Session.getUid()});

        if(c.moveToNext()){
            return c.getString(0);
        }

        return "";
    }

    public String get_description(){
        String query = "Select description from User where uid = ?";
        Cursor c = sql.rawQuery(query,new String[]{Session.getUid()});

        if(c.moveToNext()){
            return c.getString(0);
        }

        return "";
    }

}
