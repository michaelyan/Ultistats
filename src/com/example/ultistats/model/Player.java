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
	
	public static final int ALL = 1;
	public static final int PLAYER = 2;
	//This determines what uris go to this provider
	private static final UriMatcher sURIMatcher = new UriMatcher(
	        UriMatcher.NO_MATCH);
	static {
	    sURIMatcher.addURI(AUTHORITY, PLAYER_BASE_PATH + "/all", ALL);
	    sURIMatcher.addURI(AUTHORITY, PLAYER_BASE_PATH + "/#", PLAYER);
	}
	
	//A wrapper class for database storage
	public static class PlayerRow {
		private int _id;
		private String fname;
		private String lname;
		
		public PlayerRow() {}
		
		public PlayerRow(int _id, String fname, String lname) {
			this._id = _id;
			this.fname = fname;
			this.lname = lname;
		}

		public int getId() {
			return _id;
		}

		public String getfname() {
			return fname;
		}

		public String getlname() {
			return lname;
		}
		
		public void setPlayerId(int _id) {
			this._id = _id;
		}

		public void setFname(String fname) {
			this.fname = fname;
		}

		public void setLname(String lname) {
			this.lname = lname;
		}

	}

	@Override
	public boolean onCreate() {
		db = new DatabaseHelper(getContext()).getWritableDatabase();
		return true;
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
	public Cursor query(Uri uri, String[] projection, String selection,
	        String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;
	    int uriType = sURIMatcher.match(uri);
	    switch (uriType) {
	    case 1:
	    	cursor = db.rawQuery("SELECT * FROM tbl_player", null);
	        break;
	    case 2:
	    	//Since the last segment has no spaces, it will turn the string into an array of string with one element
	    	cursor = db.rawQuery("SELECT * FROM tbl_player WHERE _id = ?", uri.getLastPathSegment().split(" ", 1));
	        break;
	    default:
	        throw new IllegalArgumentException("Unknown URI");
	    }
	    return cursor;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}
}