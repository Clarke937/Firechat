package com.eretana.firechat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Edgar on 29/7/2017.
 */

public class SQLApp extends SQLiteOpenHelper implements ValueEventListener{

    private SQLiteDatabase sql;
    private DatabaseReference ref;

    public SQLApp(Context context) {
        super(context, "DBApp", null, 1);
        this.sql = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_INTERESES = "CREATE TABLE Intereses (title TEXT)";
        String CREATE_SUBINTERESES = "CREATE TABLE Subintereses (parent TEXT, child TEXT)";

        db.execSQL(CREATE_INTERESES);
        db.execSQL(CREATE_SUBINTERESES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Subintereses");
        db.execSQL("DROP TABLE IF EXISTS Intereses");
        onCreate(db);
        sync();
    }

    public void sync(){
        this.ref = new DBFirebase().get_app();
        this.ref.addListenerForSingleValueEvent(this);
    }

    private void add_interes(String interes){
        Cursor c = sql.rawQuery("Select * from Intereses where title = ?",new String[]{interes});
        if(c.getCount() == 0){
            ContentValues cv = new ContentValues();
            cv.put("title",interes);
            sql.insert("Intereses",null,cv);
        }
    }


    @Override
    public void onDataChange(DataSnapshot data) {
        //Intereses
        for(DataSnapshot ds : data.child("interest").getChildren()){
            add_interes(ds.getKey());
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }


    //GETTERS
    public List<String> get_intereses(){
        List<String> intereses = new ArrayList<>();
        Cursor c = sql.rawQuery("Select * from Intereses",null);

        while(c.moveToNext()){
            intereses.add(c.getString(0));
        }

        return intereses;
    }


}
