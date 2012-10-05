package com.example.ultistats.model;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

import java.util.ArrayList;
import java.util.List;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class Player extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    
    // Database Name
    private static final String DATABASE_NAME = "Ultistats";
 
    // Contacts table name
    private static final String TABLE_CONTACTS = "tbl_player";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_FNAME = "fname";
    private static final String KEY_LNAME = "lname";
    
    private SQLiteDatabase _db;
 
    public Player(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this._db = this.getWritableDatabase();
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
//        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
//                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
//                + KEY_PH_NO + " TEXT" + ")";
//        db.execSQL(CREATE_CONTACTS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        // Drop older table if existed
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
// 
//        // Create tables again
//        onCreate(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
    public Cursor listPlayers() {
//    	String selectQuery = "SELECT * FROM tbl_player";
//    	Cursor cursor = this._db.rawQuery(selectQuery, null);
    	
//    	if (cursor.moveToFirst()) {
//            do {
//                Log.d(cursor.getString(0), cursor.getString(1));
//                Log.d(cursor.getString(2), cursor.getString(1));
//            } while (cursor.moveToNext());
//        }
    	
//    	return cursor;
    	return null;
    }
    		
 
}
