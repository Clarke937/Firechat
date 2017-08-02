package com.eretana.firechat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Edgar on 28/6/2017.
 */

public class SQLikes extends SQLiteOpenHelper {

    private SQLiteDatabase sql;

    public SQLikes(Context context){
        super(context,"DBLikes",null,1);
        sql = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table = "Create table Hotlikes(uid TEXT)";
        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean get_status(String uid){
        String query = "Select uid from Hotlikes where uid = ?";
        Cursor c = sql.rawQuery(query,new String[]{uid});
        return c.getCount() > 0;
    }

    public void like(String uid){
        ContentValues cv = new ContentValues();
        cv.put("uid",uid);
        sql.insert("Hotlikes",null,cv);
    }

    public void dislike(String uid){
        sql.delete("Hotlikes","uid = ?",new String[]{uid});
    }

    public Cursor select(){
        Cursor c = sql.rawQuery("Select * from Hotlikes",null);
        return c;
    }




}
