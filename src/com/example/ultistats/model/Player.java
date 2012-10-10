package com.example.ultistats.model;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.ultistats.DatabaseHelper;

public class Player extends Base {

	private SQLiteDatabase db;
	
	//Must be the same name as the full class path
	private static final String AUTHORITY = "com.example.ultistats.model.Player";
	private static final String PLAYER_BASE_PATH = "players";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
	        + "/" + PLAYER_BASE_PATH);
	
	//This determines what uris go to this provider
	private static final UriMatcher sURIMatcher = new UriMatcher(
	        UriMatcher.NO_MATCH);
	static {
	    sURIMatcher.addURI(AUTHORITY, PLAYER_BASE_PATH + "/all", 1);
	    sURIMatcher.addURI(AUTHORITY, PLAYER_BASE_PATH + "/#", 2);
	}
	
	private static final String columns = "fname, lname";
	
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		db = new DatabaseHelper(getContext()).getWritableDatabase();
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
	        String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;
	    int uriType = sURIMatcher.match(uri);
	    switch (uriType) {
	    case 1:
	    	cursor = db.rawQuery("SELECT * FROM tbl_player", null);
	        break;
	    case 2:
	        break;
	    default:
	        throw new IllegalArgumentException("Unknown URI");
	    }
	    Log.i("gg", "no re");
	    return cursor;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}
}