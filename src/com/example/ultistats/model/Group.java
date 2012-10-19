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
	
	
	public static final String ALL_URI = "/all";
	public static final String PLAYERS_URI = "/players";
	public static final String NEW_URI = "/new";
	
	public static final int ALL = 1;
	public static final int PLAYERS = 2;
	public static final int GROUP = 2;
	public static final int NEW = 2;
	
	//This determines what uris go to this provider
	private static final UriMatcher sURIMatcher = new UriMatcher(
	        UriMatcher.NO_MATCH);
	static {
	    sURIMatcher.addURI(AUTHORITY, GROUP_BASE_PATH + ALL_URI, ALL);
	    sURIMatcher.addURI(AUTHORITY, GROUP_BASE_PATH + PLAYERS_URI, PLAYERS);
	    sURIMatcher.addURI(AUTHORITY, GROUP_BASE_PATH + "/#", GROUP);
	    sURIMatcher.addURI(AUTHORITY, GROUP_BASE_PATH + NEW_URI, NEW);
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
	    String query;
	    
	    switch(uriType) {
	    case ALL:
	    	query = "SELECT group_name, count(group_id) as player_count " +
	    			"FROM tbl_player_group " +
	    			"JOIN tbl_group ON tbl_group._id = tbl_player_group.group_id " +
	    			"GROUP BY group_id " +
	    			"ORDER by group_name DESC";
	    	cursor = db.rawQuery(query, null);
	        break;
	    case PLAYERS:
	    	query = "" + 
		    	"SELECT tbl_player._id, tbl_player.fname, tbl_player.lname, tbl_group._id as group_id "  +  
				"FROM tbl_player " +
				"JOIN tbl_player_group on tbl_player._id = tbl_player_group.player_id " +
				"JOIN tbl_group on tbl_group._id = tbl_player_group.group_id " +
				"ORDER BY group_name DESC";
	    	cursor = db.rawQuery(query, null);
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