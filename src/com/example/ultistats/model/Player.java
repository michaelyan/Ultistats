package com.example.ultistats.model;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.example.ultistats.DatabaseHelper;

public class Player extends Base {

	private DatabaseHelper db;
	
	//strings
	private static final String TABLE_NAME = "tbl_player";
	private static final String AUTHORITY = "com.example.ultistats.model.Player";
	public static final int TUTORIALS = 100;
	public static final int TUTORIAL_ID = 110;
	private static final String PLAYER_BASE_PATH = "players";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
	        + "/" + PLAYER_BASE_PATH);
	
	private static final UriMatcher sURIMatcher = new UriMatcher(
	        UriMatcher.NO_MATCH);
	static {
	    sURIMatcher.addURI(AUTHORITY, PLAYER_BASE_PATH, 1);
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
		db = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
	        String[] selectionArgs, String sortOrder) {
	    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
	    queryBuilder.setTables(TABLE_NAME);
	    int uriType = sURIMatcher.match(uri);
	    switch (uriType) {
	    case 1:
	        break;
	    case 2:
	        queryBuilder.appendWhere("_id="
	                + uri.getLastPathSegment());
	        break;
	    default:
	        throw new IllegalArgumentException("Unknown URI");
	    }
	    Cursor cursor = queryBuilder.query(this.db.getReadableDatabase(),
	            projection, selection, selectionArgs, null, null, sortOrder);
	    return cursor;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}
}