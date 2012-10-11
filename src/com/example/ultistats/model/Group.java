package com.example.ultistats.model;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.ultistats.DatabaseHelper;

public class Group extends Base {

	private SQLiteDatabase db;
	
	//Must be the same name as the full class path
	private static final String AUTHORITY = "com.example.ultistats.model.Group";
	private static final String GROUP_BASE_PATH = "groups";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
	        + "/" + GROUP_BASE_PATH);
	
	//This determines what uris go to this provider
	private static final UriMatcher sURIMatcher = new UriMatcher(
	        UriMatcher.NO_MATCH);
	static {
	    sURIMatcher.addURI(AUTHORITY, GROUP_BASE_PATH + "/all", 1);
	    sURIMatcher.addURI(AUTHORITY, GROUP_BASE_PATH + "/#", 2);
	}

	@Override
	public boolean onCreate() {
		db = new DatabaseHelper(getContext()).getWritableDatabase();
		return true;
	}
	
//	private static final String columns = "fname, lname";
	
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
	    	Log.i("trying", "stuff");
//	    	String query = "" + 
//		    	"SELECT group_name, group_id as _id, tbl_player.fname, tbl_player.lname"  +  
//				"FROM tbl_player " +
//				"JOIN tbl_player_group on tbl_player._id = tbl_player_group.player_id" +
//				"JOIN tbl_group on tbl_group._id = tbl_player_group.group_id";
	    	String query = "SELECT * from tbl_group";
	    	cursor = db.rawQuery(query, null);
	    	Log.i("success", "noob");
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