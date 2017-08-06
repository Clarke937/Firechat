package com.eretana.firechat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.eretana.firechat.models.Friend;

/**
 * Created by Edgar on 26/6/2017.
 */

public class SQLFriends extends SQLiteOpenHelper {

    private String table_friends = "Create table Friends (uid INTEGER, username TEXT, email TEXT)";
    private SQLiteDatabase sql;

    public SQLFriends(Context context) {
        super(context, "DBFriends", null, 1);
        sql = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(table_friends);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DELETE = "DROP TABLE IF EXISTS Friends";
        db.execSQL(DELETE);
        onCreate(db);
    }

    public void insert(Friend friend){

        ContentValues cv = new ContentValues();
        cv.put("uid",friend.getUid());
        cv.put("username",friend.getUsername());
        cv.put("email",friend.getEmail());

        sql.insert("Friends",null,cv);

    }

    public Cursor select(){
        String query = "Select * from Friends";
        return sql.rawQuery(query,null);
    }

    public void showConsole(){
        String query = "Select * from Friends";
        Cursor c = sql.rawQuery(query,null);

        while (c.moveToNext()){
            Log.e("UID: ",c.getString(0));
            Log.e("USERNAME: ",c.getString(1));
            Log.e("EMAIL: ",c.getString(2));
            Log.e("------------","--------------------");
        }

        c.close();
    }

    public void delete(String uid){
        String query = "Delete from Friends where uid = '" + uid + "'";
        sql.execSQL(query);
    }

    public void update(Friend friend){
        ContentValues cv = new ContentValues();
        cv.put("username",friend.getUsername());
        cv.put("email",friend.getEmail());
        sql.update("Friends",cv,"uid = ?",new String[]{friend.getUid()});
    }

    public boolean exist(String uid) {
        String query = "Select * from Friends where uid = '" + uid + "'";
        Cursor c = sql.rawQuery(query,null);
        return c.getCount() > 0;
    }

}
