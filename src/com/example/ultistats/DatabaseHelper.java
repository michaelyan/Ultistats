package com.example.ultistats;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.ultistats.R;
import com.example.ultistats.model.*;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	public DatabaseHelper(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	// name of the database file for your application -- change to something appropriate for your app
	private static final String DATABASE_NAME = "ultistats.sqlite";
	// any time you make changes to your database objects, you may have to increase the database version
	private static final int DATABASE_VERSION = 1;
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table tbl_player (_id integer primary key autoincrement, fname text, lname text)");
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}
